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
package org.jmangos.realm.network.netty.packet.server;

import org.apache.log4j.Logger;
import org.jmangos.realm.network.netty.packet.AbstractWoWServerPacket;

// TODO: Auto-generated Javadoc
/**
 * The Class SMSG_ADDON_INFO.
 */
public class SMSG_ADDON_INFO  extends AbstractWoWServerPacket {
	
	/** The logger. */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(SMSG_ADDON_INFO.class);

	/* (non-Javadoc)
	 * @see org.wowemu.common.network.model.SendablePacket#writeImpl()
	 */
	@Override
	protected void writeImpl() {
		byte[] addinfo = {2,1,0,0,0,0,0,0};
		for (int i = 0; i < 23; i++) {
			writeB(addinfo);
		}
		 writeD(0);
	}
}

