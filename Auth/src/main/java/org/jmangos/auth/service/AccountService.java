/*******************************************************************************
 * Copyright (C) 2012 JMaNGOS <http://jmangos.org/>
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.jmangos.auth.service;

import java.util.HashMap;

import javax.inject.Inject;

import org.jmangos.auth.dao.AccountDAO;
import org.jmangos.auth.model.Account;
import org.jmangos.auth.utils.AccountUtils;
import org.jmangos.commons.model.WoWAuthResponse;
import org.jmangos.commons.network.model.NettyNetworkChannel;
import org.jmangos.commons.utils.BigNumber;

// TODO: Auto-generated Javadoc
/**
 * This class is responsible for controlling all account actions.
 */
public class AccountService {
	
	/** The account dao. */
	@Inject
	private  AccountDAO accountDAO;
	
	/**
	 * Load clean.
	 *
	 * @param name the name
	 * @param channelHandler the channel handler
	 */
	public void loadClean(String name, NettyNetworkChannel channelHandler) {
		Account account = loadAccount(name);
		channelHandler.setChanneledObject(account);
	}
	
	/**
	 * Login.
	 *
	 * @param name the name
	 * @param channelHandler the channel handler
	 * @return the wo w auth response
	 */
	public WoWAuthResponse login(String name, NettyNetworkChannel channelHandler) {
		//if (BannedIpController.isBanned(channelHandler.getAddress().getAddress().getHostAddress())) {
		//	return WoWAuthResponse.WOW_FAIL_BANNED;
		//}
		;
		Account account = loadAccount(name);
		HashMap<String, BigNumber> variable = new HashMap<String, BigNumber>();
		BigNumber s = new BigNumber();
		BigNumber v = new BigNumber();

		if (account == null) {
			return WoWAuthResponse.WOW_FAIL_UNKNOWN_ACCOUNT;
		}
		channelHandler.setChanneledObject(account);
		if (account.getV().length() != 32 * 2
				|| account.getS().length() != 32 * 2) {
			variable = AccountUtils.setVSFields(account.getPasswordHash());
			s = variable.get("s");
			v = variable.get("v");
			updateSV(account);
		} else {
			s.setHexStr(account.getS());
			v.setHexStr(account.getV());
		}
		
		BigNumber B = AccountUtils.getB(v, channelHandler);
		account.setB_crypto(B);
		account.sets(s);
		account.setV_crypto(v);
		
		accountDAO.updateLastIp(account.getObjectId(),
				channelHandler.getAddress().getAddress().getHostAddress());
		
		return WoWAuthResponse.WOW_SUCCESS;
	}

	/**
	 * Update sv.
	 *
	 * @param account the account
	 */
	public void updateSV(Account account) {
		accountDAO.updateSecurityKey(account);
	}

	/**
	 * Loads account from DB and returns it, or returns null if account was not
	 * loaded.
	 *
	 * @param name Account name
	 * @return loaded account or null
	 */
	public Account loadAccount(String name) {
		return accountDAO.getAccount(name);
	}

	/**
	 * Update session key.
	 *
	 * @param username the username
	 * @param Key the key
	 */
	public void updateSessionKey(String username, String Key) {
		accountDAO.updateSessionKey(username, Key);
	}

	/**
	 * Gets the session key.
	 *
	 * @param username the username
	 * @return the session key
	 */
	public String getSessionKey(String username) {
		return accountDAO.getSessionKey(username);
	}
}
