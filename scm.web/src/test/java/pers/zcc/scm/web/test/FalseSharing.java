package pers.zcc.scm.web.test;

/**
 * false sharing test.<br/>
 * Running the test while ramping the number of threads and adding/removing the cache line padding,
 * compare the running time<br/>
 * eg,when NUM_THREADS = 4,padding:duration = 451306440,nopadding:duration = 21282241400<br/>
 * cache line causes deeply latency<br/>
 * compare to the origin padding way,<br/>
 * <code>
 * public final static class VolatileLong {<br/>
 * &nbsp&nbsp public volatile long value = 0L;<br/>
 * &nbsp&nbsp public long p1, p2, p3, p4, p5, p6;<br/>
 * }<br/>
 * </code>
 * the new padding format ensure value can be read in a solo cache line,not to given how many bytes
 * the class header will taken
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
        public long p1, p2, p3, p4, p5, p6, p7; // comment out

        public volatile long value = 0L;

        public long p8, p9, p10, p11, p12, p13, p14; // comment out
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
