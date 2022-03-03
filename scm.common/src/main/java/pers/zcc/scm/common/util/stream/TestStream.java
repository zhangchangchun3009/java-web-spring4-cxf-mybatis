package pers.zcc.scm.common.util.stream;

import pers.zcc.scm.common.util.stream.api.Event;
import pers.zcc.scm.common.util.stream.api.IClientHandler;
import pers.zcc.scm.common.util.stream.impl.ClientHandler;
import pers.zcc.scm.common.util.stream.impl.EventStream;
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
        EventStream stream = new EventStream().addPlugin(new IgnoreDuplicates()).addPlugin(new RoutePool());
        IClientHandler clientHandler = new ClientHandler();
        for (int i = 0; i < 1000; i++) {
            Event event;
            if ((i & 1) == 1) {
                event = new Event(i, 1);
            } else {
                event = new Event(i, 0);
            }
            stream.receiveEvent(event).consume(clientHandler);
        }
        try {
            Thread.sleep(10000);
            stream.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
