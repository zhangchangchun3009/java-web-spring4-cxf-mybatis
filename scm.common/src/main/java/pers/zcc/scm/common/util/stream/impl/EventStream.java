package pers.zcc.scm.common.util.stream.impl;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import pers.zcc.scm.common.util.stream.api.Event;
import pers.zcc.scm.common.util.stream.api.IClientHandler;
import pers.zcc.scm.common.util.stream.api.IEventStream;
import pers.zcc.scm.common.util.stream.api.IPlugin;

public class EventStream implements IEventStream {

    private Event event;

    private final IPlugin[] plugins = new IPlugin[16];

    private int lastIdx;

    private final AtomicBoolean init = new AtomicBoolean(false);

    @Override
    public EventStream receiveEvent(Event event) {
        this.event = event;
        return this;
    }

    @Override
    public EventStream addPlugin(IPlugin plugin) {
        try {
            plugins[lastIdx++] = plugin;
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("you have added too many plugins,limit 16");
        }
        return this;
    }

    private void initPlugin(IClientHandler clientHandler) {
        if (!init.get()) {
            if (lastIdx > 0) {
                plugins[lastIdx - 1].setNext(clientHandler);
                if (lastIdx >= 2) {
                    for (int i = lastIdx - 2; i >= 0; i--) {
                        plugins[i].setNext(plugins[i + 1]);
                    }
                }
            }
            init.set(true);
        } else {
            if (lastIdx > 0) {
                plugins[lastIdx - 1].setNext(clientHandler);
            }
        }
    }

    @Override
    public void consume(IClientHandler clientHandler) {
        initPlugin(clientHandler);
        if (lastIdx > 0) {
            plugins[lastIdx - 1].consume(event);
        } else {
            clientHandler.consume(event);
        }

    }

    @Override
    public void close() {
        if (lastIdx == 0) {
            return;
        }
        for (int i = 0; i < lastIdx; i++) {
            IPlugin plugin = plugins[i];
            if (plugin instanceof Closeable) {
                Closeable pluginC = (Closeable) plugin;
                try {
                    pluginC.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
