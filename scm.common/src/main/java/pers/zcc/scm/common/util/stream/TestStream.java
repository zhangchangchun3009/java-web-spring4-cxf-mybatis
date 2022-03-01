package pers.zcc.scm.common.util.stream;

import java.io.IOException;

import pers.zcc.scm.common.util.stream.api.Event;
import pers.zcc.scm.common.util.stream.api.IEventConsumer;
import pers.zcc.scm.common.util.stream.impl.ClientHandler;
import pers.zcc.scm.common.util.stream.impl.IgnoreDuplicates;
import pers.zcc.scm.common.util.stream.impl.RoutePool;

public class TestStream {

    public static void main(String[] args) {
        IEventConsumer clientHandler = new ClientHandler();
        IEventConsumer ignoreDuplicates = new IgnoreDuplicates(clientHandler);
        RoutePool routePool = new RoutePool(ignoreDuplicates);
        System.out.println(System.currentTimeMillis());
        for (int i = 0; i < 1000; i++) {
            Event event;
            if ((i & 1) == 1) {
                event = new Event(i, 1);
            } else {
                event = new Event(i, 0);
            }
            routePool.consume(event);
        }
        System.out.println(System.currentTimeMillis());
        try {
            Thread.sleep(10000);
            routePool.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
