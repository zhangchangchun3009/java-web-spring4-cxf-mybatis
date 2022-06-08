package pers.zcc.scm.common.frame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.SystemUtils;

/**
 * a simple log service, origin code is in the book "java concurrency in practice".
 * make a little change
 * @author zhangchangchun
 * @Date 2021年10月
 */
public class LogService {

    private static final LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>(3000); // change size for queue test

    private final AtomicBoolean shutdown = new AtomicBoolean(false);

    private PrintWriter writer;

    private LogThread logger;

    public void start() throws FileNotFoundException {
        writer = new PrintWriter(SystemUtils.IS_OS_WINDOWS ? "d:\\log.txt" : "/usr/zcc/log.txt");
        logger = new LogService.LogThread();
        logger.start();
    }

    public void stop() {
        if (shutdown.compareAndSet(false, true)) {
            /** 
             * let the logger thread check shutdown flag immediately 
             * so it will stop as soon as possible if the queue is empty;
             * if it is not, log action continues
            */
            logger.interrupt();
        }
    }

    public void log(String message) throws InterruptedException, IllegalStateException {
        if (shutdown.get()) {
            throw new IllegalStateException("logger is shutting down now,can't log more");
        }
        messageQueue.put(message);
    }

    class LogThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    try {
                        // both values are volatile, no need for a lock? according to the test results this seems okay
                        if (shutdown.get() && messageQueue.size() == 0) {
                            break;
                        }
                        String message = messageQueue.take();
                        writer.write(message);
                        // Thread.sleep(20); // uncomment it for the test 'finish all tasks before stop'
                    } catch (InterruptedException e) {
                        // ignore and continue
                    }
                }
            } finally {
                writer.close();
            }

        }
    }

    public static void main(String[] args) {
        LogService logger = new LogService();
        try {
            logger.start();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return;
        }
        for (int j = 0; j < 10; j++) {
            final int k = j;
            new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    try {
                        logger.log("t-" + k + "-line" + i + "\r\n");
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }).start();
        }
        try {
            Thread.sleep(10); // waiting for enqueue tasks
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.stop();
    }
}
