package pers.zcc.scm.common.frame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.SystemUtils;

public class LogService {

    private static final LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>(3000);

    private final AtomicBoolean shutdown = new AtomicBoolean(false);

    private PrintWriter writer;

    private LogThread logger;

    public void start() {
        try {
            writer = new PrintWriter(SystemUtils.IS_OS_WINDOWS ? "d:\\log.txt" : "/usr/zcc/log.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            new IllegalArgumentException("file path is incorrect");
        }
        logger = new LogService.LogThread();
        logger.start();
    }

    public void stop() {
        shutdown.compareAndSet(false, true);
        logger.interrupt();
    }

    public void log(String message) {
        synchronized (this) {
            if (shutdown.get()) {
                throw new IllegalStateException("logger is shutting down now,can't log more");
            }
        }
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class LogThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    try {
                        synchronized (LogService.this) {
                            if (shutdown.get() && messageQueue.size() == 0) {
                                break;
                            }
                        }
                        String message = messageQueue.take();
                        writer.write(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                writer.close();
            }

        }
    }

    public static void main(String[] args) {
        LogService logger = new LogService();
        logger.start();
        for (int i = 0; i < 10; i++) {
            logger.log("line" + i + "\n");
        }
        logger.stop();
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
