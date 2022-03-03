package pers.zcc.scm.common.util.stream.api;

public interface IPlugin extends IEventConsumer {

    void setNext(IEventConsumer nextConsumer);

}
