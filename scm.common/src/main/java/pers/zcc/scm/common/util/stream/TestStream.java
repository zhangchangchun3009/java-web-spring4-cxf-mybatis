package pers.zcc.scm.common.util.stream;

import java.io.IOException;

import pers.zcc.scm.common.util.stream.api.Event;
import pers.zcc.scm.common.util.stream.api.IEventConsumer;
import pers.zcc.scm.common.util.stream.impl.ClientHandler;
import pers.zcc.scm.common.util.stream.impl.IgnoreDuplicates;
import pers.zcc.scm.common.util.stream.impl.RoutePool;

/**
 * a test of the paraller event handler stream api
 * see <link>http://ifeve.com/part-1-thread-pools/</link>
 * @originframeauthor unknow
 * @author zhangchangchun integrate code, do a little change and test it
 * @Date 2022年3月1日
 */
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
