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
package org.jmangos.realm.network.netty.packetClient.client;

import java.nio.BufferUnderflowException;

import org.jmangos.realm.network.netty.packetClient.AbstractWoWClientPacket;

// TODO: Auto-generated Javadoc
/**
 * The Class CMSG_SET_ACTIONBAR_TOGGLES.
 */
public class CMSG_SET_ACTIONBAR_TOGGLES extends AbstractWoWClientPacket {
    
    /** The action bar. */
    byte actionBar;
    
    /*
     * (non-Javadoc)
     * 
     * @see org.wowemu.common.network.model.ReceivablePacket#readImpl()
     */
    @Override
    protected void readImpl() throws BufferUnderflowException, RuntimeException {
    
        this.actionBar = (byte) readC();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.wowemu.common.network.model.ReceivablePacket#runImpl()
     */
    @Override
    protected void runImpl() {
    
        // FIXME need complete stats
        if (getPlayer() != null) {
            // getPlayer().setByteValue()
        }
        
    }
    
}
