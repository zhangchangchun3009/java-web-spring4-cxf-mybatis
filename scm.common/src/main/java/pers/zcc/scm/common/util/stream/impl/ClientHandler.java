package pers.zcc.scm.common.util.stream.impl;

import pers.zcc.scm.common.util.stream.api.Event;
import pers.zcc.scm.common.util.stream.api.IEventConsumer;

public class ClientHandler implements IEventConsumer {

    @Override
    public Event consume(Event event) {
        try {
            Sleeper.randomSleep(10, 1);
            System.out.println(event.getClientId());
            System.out.println(event.getEventId());
            System.out.println("**************************");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return event;
    }

}
