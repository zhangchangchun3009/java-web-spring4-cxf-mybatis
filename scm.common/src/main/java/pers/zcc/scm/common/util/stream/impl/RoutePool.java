package pers.zcc.scm.common.util.stream.impl;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pers.zcc.scm.common.util.stream.api.Event;
import pers.zcc.scm.common.util.stream.api.IEventConsumer;

public class RoutePool implements IEventConsumer, Closeable {

    private final IEventConsumer nextConsumer;

    private final List<ExecutorService> executors;

    public RoutePool(IEventConsumer consumer) {
        List<LinkedBlockingDeque<Runnable>> queueList = IntStream.range(0, 10).mapToObj((i) -> {
            return new LinkedBlockingDeque<Runnable>(3000);
        }).collect(Collectors.toList());
        List<ThreadPoolExecutor> executorList = queueList.stream()
                .map((queue) -> new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, queue))
                .collect(Collectors.toList());
        this.executors = new CopyOnWriteArrayList<ExecutorService>(executorList);
        this.nextConsumer = consumer;

    }

    @Override
    public void close() throws IOException {
        executors.forEach(ExecutorService::shutdown);
    }

    @Override
    public Event consume(Event event) {
        int threadIdx = event.getClientId() % executors.size();
        final ExecutorService executor = executors.get(threadIdx);
        executor.submit(() -> nextConsumer.consume(event));
        return event;
    }

}
