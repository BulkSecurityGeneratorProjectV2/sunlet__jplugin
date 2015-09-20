package net.jplugin.core.event.impl;

import java.util.Hashtable;
import java.util.Vector;

import net.jplugin.core.event.api.Channel;
import net.jplugin.core.event.api.Event;
import net.jplugin.core.event.api.EventAliasDefine;
import net.jplugin.core.event.api.EventConsumer;
import net.jplugin.core.event.api.IEventFilter;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-7 ����03:29:38
 **/

public class ChannelFacade extends Channel {
	//Key���¼����ͻ��߱������ͣ�Value���Ƿ������true-��������  False��ԭʼEvent����
	Hashtable<String, Boolean> eventTypesMap = new Hashtable<String, Boolean>();
	Hashtable<String, Vector<AliasAndFilter>> filterMapping = new Hashtable<String, Vector<AliasAndFilter>>();
	Hashtable<String,Vector<Channel>> channelMap = new Hashtable<String, Vector<Channel>>();

	static class AliasAndFilter{
		String alias;
		IEventFilter filter;
		AliasAndFilter(String a,IEventFilter f){
			this.alias = a;
			this.filter = f;
		}
	}
	
	public void init(String[] eventTypes,EventAliasDefine[] typeAliases, EventConsumer[] consumers){
		for (String e:eventTypes){
			if (eventTypesMap.containsKey(e)){
				throw new RuntimeException("Duplicated event type:e");
			}
			eventTypesMap.put(e, false);
		}
		
		//���alias,����ʼ��filterMapping
		for (EventAliasDefine ead:typeAliases){
			if (!eventTypesMap.containsKey(ead.getEventType())){
				throw new RuntimeException("Can't find event type ["+ead.getEventType()+"] for alias type ["+ead.getTypeAlias()+"]");
			}
			if (eventTypesMap.containsKey(ead.getTypeAlias())){
				throw new RuntimeException("Error alias name ["+ead.getTypeAlias()+"],duplicate with event type");
			}
			try {
				addAdiasAndFilter(ead.getEventType(),new AliasAndFilter(ead.getTypeAlias(),(IEventFilter) ead.getFilterClass().newInstance()));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		//Alias���ͼ���eventTypesMap��
		for (EventAliasDefine ead:typeAliases){
			if (eventTypesMap.containsKey(ead.getTypeAlias())){
				throw new RuntimeException("Duplicated event alias type:"+ead.getTypeAlias());
			}
			eventTypesMap.put(ead.getTypeAlias(), true);
		}
		
		//��ʼ��consumer
		for (EventConsumer c:consumers){
			if (!eventTypesMap.containsKey(c.getTargetType())){
				throw new RuntimeException("Can't find event type or event alias type for consumer:"+c.getClass().getName());
			}
			addConsumerToChannel(c);
		}
	}
	
	/**
	 * @param string 
	 * @param aliasAndFilter
	 */
	private void addAdiasAndFilter(String eventType, AliasAndFilter aliasAndFilter) {
		 //get the list
		 Vector<AliasAndFilter> list = filterMapping.get(eventType);
		 if (list == null){
			 list =  new Vector<AliasAndFilter>();
			 filterMapping.put(eventType, list);
		 }
		 
		 //add to list
		 list.add(aliasAndFilter);
	}

	/**
	 * @param c
	 */
	private void addConsumerToChannel(EventConsumer c) {
		 String eventRouteType = c.getTargetType();
		 
		 //��ʼ�������͵�channel�б�
		 Vector<Channel> channels = channelMap.get(eventRouteType);
		 if (channels == null){
			 channels =  new Vector<Channel>();
			 channelMap.put(eventRouteType, channels);
		 }
		 
		 //����targetChannel
		 Channel targetChannel = null;
		 for (Channel channel:channels){
			 if (channel.getChannelType().equals(c.getChannelType())){
				 targetChannel = channel;
				 break;
			 }
		 }
		 if (targetChannel == null){
			 targetChannel = creteChannel(c.getChannelType());
			 channels.add(targetChannel);
		 }
		 
		 //����consumer
		 targetChannel.addConsumer(c);
	}

	/**
	 * @param channelType
	 * @return
	 */
	private Channel creteChannel(ChannelType channelType) {
		return ChannelFactory.createChannel(channelType);
	}

	@Override
	public void sendEvent(Event e) {

		Boolean isAliasType = eventTypesMap.get(e.getType());
		
		if (isAliasType == null){
			//�����ڶ�Ӧ��Event����Alias
			throw new RuntimeException("The event type not regist:"+e.getType());
		}
		if (isAliasType == true){
			//��һ��Alias
			throw new RuntimeException("The event type not regist:"+e.getType()+" It's a alias type");
		}
		
		executeOnChannel(e.getType(),e);
		
		//�����event����alias����Ҫ���͸�alias
		Vector<AliasAndFilter> list = filterMapping.get(e.getType());
		if (list!=null){
			for (AliasAndFilter aaf:list){
				if (aaf.filter.match(e)){
					executeOnChannel(aaf.alias,e);
				}
			}
		}
	}

	/**
	 * @param e
	 */
	private void executeOnChannel(String routeTpe,Event e) {
		Vector<Channel> channel = channelMap.get(routeTpe);
		if (channel == null){
			return;
		}
		for (Channel c:channel){
			c.sendEvent(e);
		}
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.event.api.Channel#getChannelType()
	 */
	@Override
	public ChannelType getChannelType() {
		throw new RuntimeException("Can't call this method on ChnnelFacade");
	}
}

