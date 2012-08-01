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
package org.jmangos.auth.network.netty.handler;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandler;
import org.jmangos.commons.network.model.ConnectHandler;
import org.jmangos.commons.network.model.NettyNetworkChannel;
import org.jmangos.commons.network.model.State;

// TODO: Auto-generated Javadoc
/**
 * The Class L2CConnectHandler.
 */
public class L2CConnectHandler implements ConnectHandler{

	/** The Constant log. */
	private static final Logger             log     = Logger.getLogger(L2CConnectHandler.class);

	/* (non-Javadoc)
	 * @see org.wowemu.common.network.model.ConnectHandler#onConnect(org.wowemu.common.network.model.NettyNetworkChannel, org.jboss.netty.channel.ChannelHandler)
	 */
	@Override
	public void onConnect(NettyNetworkChannel networkChannel,
			ChannelHandler handler) {
		networkChannel.setChannelState(State.CONNECTED);
		
		log.info("Accepting connection from: "+ networkChannel.getAddress().getHostName());
		
	}

	/* (non-Javadoc)
	 * @see org.wowemu.common.network.model.ConnectHandler#onDisconnect(org.wowemu.common.network.model.NettyNetworkChannel)
	 */
	@Override
	public void onDisconnect(NettyNetworkChannel networkChannel) {
		log.info("Disconnection : "+ networkChannel.getAddress().getHostName());
		
	}



}
