package net.jplugin.core.event.api;

import net.jplugin.core.event.api.Channel.ChannelType;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-7 ����03:25:25
 **/

public abstract class EventConsumer {
	private String targetType;
	private ChannelType channelType;

	public  EventConsumer(){
	}

	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public ChannelType getChannelType() {
		return channelType;
	}
	public void setChannelType(ChannelType channelType) {
		this.channelType = channelType;
	}

	public abstract void consume(Event e);
}
