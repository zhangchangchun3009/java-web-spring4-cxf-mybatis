package pers.zcc.scm.common.util.stream.api;

public interface IEventStream {
    void consume(IEventConsumer consumer);
}
