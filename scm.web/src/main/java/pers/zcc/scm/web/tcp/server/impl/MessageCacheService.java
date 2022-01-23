package pers.zcc.scm.web.tcp.server.impl;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Named;

import io.netty.channel.Channel;

@Named
public class MessageCacheService {
    private Map<String, Channel> channels = new ConcurrentHashMap<>();

    public void put(String unitId, Channel channel) {
        channels.put(unitId, channel);
    }

    public void remove(String unitId) {
        channels.remove(unitId);
    }

    public void remove(Channel channel) {
        Set<String> longs = channels.keySet();
        for (String unitId : longs) {
            if (channels.get(unitId).equals(channel)) {
                channels.remove(unitId);
                break;
            }
        }
    }

    public Channel getChannel(String unitId) {
        return channels.get(unitId);
    }

    private Map<String, String> messages = new ConcurrentHashMap<>();

    public void putMessage(String messageId, String json) {
        messages.put(messageId, json);
    }

    public String getMessage(String messageId) {
        String message = messages.get(messageId);
        if (message != null)
            messages.remove(messageId);
        return message;
    }
    //TODO:增加定时任务，将过期的message清除
}
