package org.jmangos.auth.controller;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.ArrayUtils;
import org.criteria4jpa.criterion.Criterion;
import org.criteria4jpa.criterion.Restrictions;
import org.jmangos.auth.model.AccountDto;
import org.jmangos.auth.services.impl.AccountServiceImpl;
import org.jmangos.auth.utils.AccountUtils;
import org.jmangos.commons.model.Account;
import org.jmangos.commons.model.WoWAuthResponse;
import org.jmangos.commons.network.model.NettyNetworkChannel;
import org.jmangos.commons.utils.BigNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AccountController {
    
    /** The Constant logger. */
    private static final Logger        logger   = LoggerFactory.getLogger(AccountController.class);
    
    @Inject
    private AccountServiceImpl         accountService;
    
    private final Map<String, Account> accounts = new HashMap<String, Account>();
    
    /**
     * Login.
     * 
     * @param name
     *            the name
     * @param channelHandler
     *            the channel handler
     * @return the wow auth response
     */
    public WoWAuthResponse loadLogin(final String name, final NettyNetworkChannel channelHandler) {
    
        // if
        // (BannedIpController.isBanned(channelHandler.getAddress().getAddress().getHostAddress()))
        // {
        // return WoWAuthResponse.WOW_FAIL_BANNED;
        // }
        
        final Account account = new Account();
        
        final Criterion criterion = Restrictions.eq("username", name);
        final List<AccountDto> accountList = this.accountService.readAccounts(criterion);
        if ((accountList != null) && !accountList.isEmpty()) {
            final AccountDto accountDto = accountList.get(0);
            
            HashMap<String, BigNumber> variable; // calculateVSFields will create it.
            BigNumber s = new BigNumber();
            BigNumber v = new BigNumber();
            
            this.accounts.put(name, account);
            channelHandler.setChanneledObject(account);
            if ((accountDto.getV().length() != (32 * 2)) || (accountDto.getS().length() != (32 * 2))) {
                variable = AccountUtils.calculateVSFields(accountDto.getShaPasswordHash());
                s = variable.get("s");
                v = variable.get("v");
                accountDto.setS(s.asHexStr());
                accountDto.setV(v.asHexStr());
            } else {
                s.setHexStr(accountDto.getS());
                v.setHexStr(accountDto.getV());
            }
            
            final BigNumber B = AccountUtils.getB(v, channelHandler);
            account.setName(name);
            account.setB_crypto(B);
            account.sets(s);
            account.setV_crypto(v);
            account.setAccessLevel(accountDto.getGmlevel());
            accountDto.setLastIp(channelHandler.getAddress().getAddress().getHostAddress());
            
            this.accountService.createOrUpdateAccount(accountDto);
            
            return WoWAuthResponse.WOW_SUCCESS;
        } else {
            return WoWAuthResponse.WOW_FAIL_UNKNOWN_ACCOUNT;
        }
    }
    
    public WoWAuthResponse checkPassword(final Account account, final byte[] a, final byte[] m1) {
    
        logger.debug("a length " + a.length);
        logger.debug("a value " + new BigInteger(1, a).toString(16).toUpperCase());
        logger.debug("m1 length " + m1.length);
        logger.debug("m1 value " + new BigInteger(1, m1).toString(16).toUpperCase());
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
            return WoWAuthResponse.WOW_FAIL_INCORRECT_PASSWORD;
        }
        final BigNumber B = account.getcryptoB();
        logger.debug("B value " + B.asHexStr());
        sha.update(a);
        sha.update(B.asByteArray(32));
        final BigNumber u = new BigNumber();
        u.setBinary(sha.digest());
        logger.debug("u value" + u.asHexStr());
        final BigNumber A = new BigNumber();
        A.setBinary(a);
        logger.debug("A:" + A.asHexStr());
        final BigNumber S = A.multiply((account.getV_crypto().modPow(u, AccountUtils.N))).modPow(account.getB(), AccountUtils.N);
        
        final byte[] t1 = new byte[16];
        final byte[] vK = new byte[40];
        
        final byte[] t = S.asByteArray(32);
        for (int i = 0; i < 16; ++i) {
            t1[i] = t[i * 2];
        }
        sha.update(t1);
        byte[] t2 = sha.digest();
        for (int i = 0; i < 20; ++i) {
            vK[i * 2] = t2[i];
        }
        for (int i = 0; i < 16; ++i) {
            t1[i] = t[(i * 2) + 1];
        }
        sha.update(t1);
        t2 = sha.digest();
        for (int i = 0; i < 20; ++i) {
            vK[(i * 2) + 1] = t2[i];
        }
        
        byte[] hash = new byte[20];
        logger.debug("N:" + AccountUtils.N.asHexStr());
        sha.update(AccountUtils.N.asByteArray(32));
        hash = sha.digest();
        logger.debug("hash:" + new BigInteger(1, hash).toString(16).toUpperCase());
        sha.update(AccountUtils.g.asByteArray(1));
        final byte[] gH = sha.digest();
        for (int i = 0; i < 20; ++i) {
            hash[i] ^= gH[i];
        }
        
        byte[] t4 = new byte[20];
        sha.update(account.getName().toUpperCase().getBytes(Charset.forName("UTF-8")));
        t4 = sha.digest();
        
        sha.update(hash);
        sha.update(t4);
        sha.update(account.getS_crypto().asByteArray(32));
        sha.update(A.asByteArray(32));
        sha.update(B.asByteArray(32));
        sha.update(vK);
        
        final byte[] sh = sha.digest();
        
        if (Arrays.equals(sh, m1)) {
            sha.update(A.asByteArray(32));
            sha.update(sh);
            sha.update(vK);
            
            account.setM2(sha.digest());
            account.setvK(vK);
            ArrayUtils.reverse(vK);
            
            final Criterion criterion = Restrictions.eq("username", account.getName());
            final List<AccountDto> accountList = this.accountService.readAccounts(criterion);
            if ((accountList != null) && !accountList.isEmpty()) {
                final AccountDto accountDto = accountList.get(0);
                final String sessionKey = new BigInteger(1, vK).toString(16).toUpperCase();
                accountDto.setSessionKey(sessionKey);
                this.accountService.createOrUpdateAccount(accountDto);
            } else {
                return WoWAuthResponse.WOW_FAIL_INCORRECT_PASSWORD;
            }
            return WoWAuthResponse.WOW_SUCCESS;
        } else {
            return WoWAuthResponse.WOW_FAIL_INCORRECT_PASSWORD;
        }
    }
    
    public boolean checkSessionKey(final Account account, final byte[] R1, final byte[] R2) {
    
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        
        final Criterion criterion = Restrictions.eq("username", account.getName());
        final List<AccountDto> accountList = this.accountService.readAccounts(criterion);
        if ((accountList != null) && !accountList.isEmpty()) {
            final AccountDto accountDto = accountList.get(0);
            final String sessionKey = accountDto.getSessionKey();
            
            sha.update(account.getName().getBytes());
            sha.update(R1);
            sha.update(account.get_reconnectProof().asByteArray(16));
            sha.update(convertSessionKey(sessionKey));
        } else {
            return false;
        }
        
        if (Arrays.equals(sha.digest(), R2)) {
            return true;
        } else {
            return false;
        }
    }
    
    public Account getAccount(final String login) {
    
        final Criterion criterion = Restrictions.eq("username", login);
        final List<AccountDto> accountList = this.accountService.readAccounts(criterion);
        if ((accountList != null) && !accountList.isEmpty()) {
            final AccountDto accountDto = accountList.get(0);
            Account account = new Account();
            account.setId(accountDto.getId());
            account.setName(accountDto.getUsername());
            account.setSessionKey(accountDto.getSessionKey());
            return account;
        }
        return null;
    }
    
    /**
     * Convert to session key.
     * 
     * @param hexkey
     * 
     * @return the byte[]
     */
    private byte[] convertSessionKey(final String hexkey) {
    
        final int len = hexkey.length();
        final byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[((len - i) / 2) - 1] = (byte) ((Character.digit(hexkey.charAt(i), 16) << 4) + Character.digit(hexkey.charAt(i + 1), 16));
        }
        return data;
    }
    
    /**
     * Load clean.
     * 
     * @param name
     *            the name
     * @param channelHandler
     *            the channel handler
     */
    public void loadClean(final String name, final NettyNetworkChannel channelHandler) {
    
        final Account account = this.accounts.get(name);
        channelHandler.setChanneledObject(account);
    }
    
}
