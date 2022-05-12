package pers.zcc.scm.web.test;

/**
 * on my pc, cpu type is AMD R7-5800H, with L1 cache 512kb, L2 4mb, L3 16mb
 * 
 * when fill with 12 byte arrays, fields were not declared as final, result is:
 * one while cost:11962467700ns
 * two while cost:9734908200ns
 * one while cost:11905158500ns
 * two while cost:9603712701ns
 * one while cost:12051737601ns
 * two while cost:10664304800ns
 * 
 * after fields were declared as final:
 * one while cost:7356742300ns
 * two while cost:5992797600ns
 * one while cost:7330684700ns
 * two while cost:6076398400ns
 * one while cost:7374705900ns
 * two while cost:6156816900ns
 * 
 * when fill with 6 byte arrays(keep up with the article author's code, Intel cpu), 
 * fields were not declared as final, result is:
 * 
 * one while cost:4163091800ns
 * two while cost:5204546099ns
 * one while cost:4041918799ns
 * two while cost:5214706800ns
 * one while cost:4139240099ns
 * two while cost:5331539099ns.
 * 
 * after fields were declared as final:
 * one while cost:2994423700ns
 * two while cost:5365098600ns
 * one while cost:3021756600ns
 * two while cost:5357733300ns
 * one while cost:3013036700ns
 * two while cost:5274406000ns
 *
 * @author zhangchangchun
 * @see http://ifeve.com/writecombining/
 * @see https://stackoverflow.com/questions/49959963/where-is-the-write-combining-buffer-located-x86
 * @Date 2022年5月12日
 */
public class WriteCombiningTest {
    //all fields final or not
    static final int iterate = Integer.MAX_VALUE;

    static final int size = 1 << 24;

    static final int mask = size - 1;

    static final byte[] byte1 = new byte[size];

    static final byte[] byte2 = new byte[size];

    static final byte[] byte3 = new byte[size];

    static final byte[] byte4 = new byte[size];

    static final byte[] byte5 = new byte[size];

    static final byte[] byte6 = new byte[size];

    static final byte[] byte7 = new byte[size];

    static final byte[] byte8 = new byte[size];

    static final byte[] byte9 = new byte[size];

    static final byte[] byte10 = new byte[size];

    static final byte[] byte11 = new byte[size];

    static final byte[] byte12 = new byte[size];

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            caseOne();
            caseTwo();
//            case3();
//            case4();
        }

    }

    static void caseOne() {
        int i = iterate;
        long t1 = System.nanoTime();
        while (--i > 0) {
            int slot = i & mask;
            byte value = (byte) i;
            byte1[slot] = value;
            byte2[slot] = value;
            byte3[slot] = value;
            byte4[slot] = value;
            byte5[slot] = value;
            byte6[slot] = value;
            byte7[slot] = value;
            byte8[slot] = value;
            byte9[slot] = value;
            byte10[slot] = value;
            byte11[slot] = value;
            byte12[slot] = value;
        }
        System.out.println("one while cost:" + (System.nanoTime() - t1) + "ns");
    }

    static void caseTwo() {
        int i = iterate;
        long t1 = System.nanoTime();
        while (--i > 0) {
            int slot = i & mask;
            byte value = (byte) i;
            byte1[slot] = value;
            byte2[slot] = value;
            byte3[slot] = value;
            byte4[slot] = value;
            byte5[slot] = value;
            byte6[slot] = value;
        }
        i = iterate;
        while (--i > 0) {
            int slot = i & mask;
            byte value = (byte) i;
            byte7[slot] = value;
            byte8[slot] = value;
            byte9[slot] = value;
            byte10[slot] = value;
            byte11[slot] = value;
            byte12[slot] = value;
        }
        System.out.println("two while cost:" + (System.nanoTime() - t1) + "ns");
    }

    static void case3() {
        int i = iterate;
        long t1 = System.nanoTime();
        while (--i > 0) {
            int slot = i & mask;
            byte value = (byte) i;
            byte1[slot] = value;
            byte2[slot] = value;
            byte3[slot] = value;
            byte4[slot] = value;
            byte5[slot] = value;
            byte6[slot] = value;
        }
        System.out.println("one while cost:" + (System.nanoTime() - t1) + "ns");
    }

    static void case4() {
        int i = iterate;
        long t1 = System.nanoTime();
        while (--i > 0) {
            int slot = i & mask;
            byte value = (byte) i;
            byte1[slot] = value;
            byte2[slot] = value;
            byte3[slot] = value;
        }
        i = iterate;
        while (--i > 0) {
            int slot = i & mask;
            byte value = (byte) i;
            byte4[slot] = value;
            byte5[slot] = value;
            byte6[slot] = value;
        }
        System.out.println("two while cost:" + (System.nanoTime() - t1) + "ns");
    }

}
