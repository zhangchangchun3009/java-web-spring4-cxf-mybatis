package pers.zcc.scm.common.util.stream.api;

import pers.zcc.scm.common.util.stream.impl.EventStream;

public interface IEventStream {
    EventStream receiveEvent(Event event);

    EventStream addPlugin(IPlugin plugin);

    void consume(IClientHandler clientHandler);

    void close();
}
