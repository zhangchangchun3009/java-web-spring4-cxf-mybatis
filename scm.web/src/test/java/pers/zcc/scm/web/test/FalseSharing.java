package pers.zcc.scm.web.test;

/**
 * false sharing test
 * Running the test while ramping the number of threads and adding/removing the cache line padding,
 * thats line 16,line 46
 * compare the running time
 * eg,when NUM_THREADS = 4,padding:duration = 451306440,nopadding:duration = 21282241400
 * cache line causes deeply latency
 * @originAuthor Martin Thompson
 * @author zhangchangchun
 * @Date 2022年3月2日
 */
public class FalseSharing implements Runnable {

    public final static int NUM_THREADS = 4; // change

    public final static long ITERATIONS = 500L * 1000L * 1000L;

    private final int arrayIndex;

    private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];

    public FalseSharing(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    static {
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new VolatileLong();
        }
    }

    @Override
    public void run() {
        long i = ITERATIONS + 1;
        while (0 != --i) {
            longs[arrayIndex].value = i;
        }

    }

    public final static class VolatileLong {
        public volatile long value = 0L;

        public long p1, p2, p3, p4, p5, p6; // comment out
    }

    public static void main(String[] args) throws Exception {
        final long start = System.nanoTime();
        runTest();
        System.out.println("duration = " + (System.nanoTime() - start));
    }

    private static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
    }

}
