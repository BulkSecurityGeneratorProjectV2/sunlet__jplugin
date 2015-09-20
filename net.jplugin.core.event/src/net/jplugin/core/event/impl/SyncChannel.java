package net.jplugin.core.event.impl;

import net.jplugin.core.event.api.Channel;
import net.jplugin.core.event.api.Event;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-9 ����11:47:59
 **/

public class SyncChannel extends Channel{

	/* (non-Javadoc)
	 * @see net.luis.plugin.event.api.Channel#getChannelType()
	 */
	@Override
	public ChannelType getChannelType() {
		return Channel.ChannelType.SYNC;
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.event.api.Channel#sendEvent(net.luis.plugin.event.api.Event)
	 */
	@Override
	public void sendEvent(Event e) {
		for (int i=0;i<this.consumers.size();i++){
			this.consumers.get(i).consume(e);
		}
	}

}
