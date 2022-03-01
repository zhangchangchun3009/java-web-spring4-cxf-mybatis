package pers.zcc.scm.common.util.stream.impl;

import java.time.Duration;

import org.ehcache.Cache;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import pers.zcc.scm.common.util.EhCacheUtil;
import pers.zcc.scm.common.util.stream.api.Event;
import pers.zcc.scm.common.util.stream.api.IEventConsumer;

public class IgnoreDuplicates implements IEventConsumer {
    private final IEventConsumer nextConsumer;

    Cache<Integer, String> intToStringCache = EhCacheUtil.getManager().createCache("IntToStringCache",
            CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Integer.class, String.class, ResourcePoolsBuilder.heap(100))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMillis(10000))));

    public IgnoreDuplicates(IEventConsumer nextConsumer) {
        this.nextConsumer = nextConsumer;
    }

    @Override
    public Event consume(Event event) {
        int id = event.getEventId();
        if (intToStringCache.putIfAbsent(id, "") == null) {
            nextConsumer.consume(event);
        }
        return event;
    }

}
