
package pers.zcc.scm.common.util.modbustcp.slave.db;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import org.springframework.transaction.TransactionUsageException;

/**
 * modbus slave 数据池管理系统
 * @author zcc
 * @since 2021年12月13日
 */
public class ModbusPoolDBMS {

    private static Map<String, ModbusPoolDB> instanceHolder = new ConcurrentHashMap<>(4);

    private ModbusPoolDBMS() {
    }

    public static void createInstance(String unitId, int capacity) {
        instanceHolder.putIfAbsent(unitId, new ModbusPoolDB(capacity));
    }

    public static ModbusPoolDB getInstance(String unitId) {
        return instanceHolder.get(unitId);
    }

    /**
     * 从当前地址开始(包括)读取n个字.
     *
     * @param add the add
     * @param quantity the quantity
     * @param unitId the unit id
     * @return the short[]
     * @throws TimeoutException 当前地址被写锁定，等待释放超时
     * @throws IndexOutOfBoundsException 输入有误
     */
    public static short[] readHoldingRegister(int add, int quantity, String unitId)
            throws TimeoutException, IndexOutOfBoundsException {
        return getInstance(unitId).readHoldingRegister(add, quantity);
    }

    /**
     * 往当前地址写入一个字.
     *
     * @param add the add
     * @param value the value
     * @param writeTransactionId the write transaction id
     * @param unitId the unit id
     * @throws TransactionUsageException 当前地址被其它事务线程写入锁定
     * @throws IndexOutOfBoundsException 输入有误
     */
    public static void writeSingleRegister(int add, short value, int writeTransactionId, String unitId)
            throws TransactionUsageException, IndexOutOfBoundsException {
        getInstance(unitId).writeSingleRegister(add, value, writeTransactionId);
    }

    /**
     * 往当前地址（包含）写入多个字.
     *
     * @param add the add
     * @param quantity the quantity
     * @param value the value
     * @param writeTransactionId the write transaction id
     * @param unitId the unit id
     * @throws TransactionUsageException 当前地址区间被其它事务线程写入锁定
     * @throws IndexOutOfBoundsException 输入有误
     */
    public static void writeMultiRegister(int add, int quantity, short[] value, int writeTransactionId, String unitId)
            throws TransactionUsageException, IndexOutOfBoundsException {
        getInstance(unitId).writeMultiRegister(add, quantity, value, writeTransactionId);
    }

    private static class ModbusPoolDB {
        /** The holding registers. */
        private short[] holdingRegisters;

        private volatile int[] writeLocks;

        private Object lock = new Object();

        public ModbusPoolDB(int capacity) {
            this.holdingRegisters = new short[capacity];
            writeLocks = new int[capacity];
        }

        /**
         * @param add
         * @param quantity
         * @param writeTransactionId
         * @return -1 if set lock successful,otherwise return the transctionId by which the address is locked
         */
        private void setWriteLock(int add, int quantity, int writeTransactionId) {
            for (int i = 0; i < quantity; i++) {
                writeLocks[add + i] = writeTransactionId;
            }
        }

        private int checkLock(int add, int quantity, int writeTransactionId) {
            for (int i = 0; i < quantity; i++) {
                int transctionId = writeLocks[add + i];
                if (transctionId != 0 && transctionId != writeTransactionId) {
                    return transctionId;
                }
            }
            return -1;
        }

        public short[] readHoldingRegister(int add, int quantity) throws TimeoutException, IndexOutOfBoundsException {
            if (add < 0 || quantity <= 0 || add + quantity > holdingRegisters.length) {
                throw new IndexOutOfBoundsException();
            }
            short[] res = new short[quantity];
            int count = 0;
            int cnt = 0;
            for (int i = 0; i < quantity; i++) {
                cnt = 0;
                while (true) {
                    if (cnt > 5 || count > 20) {
                        throw new TimeoutException();
                    }
                    int writeLock = writeLocks[add + i];
                    if (writeLock != 0) {
                        cnt++;
                        count++;
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                        }
                        continue;
                    }
                    res[i] = holdingRegisters[add + i];
                    break;
                }
            }

            return res;
        }

        private void unlockWriteLock(int add, int quantity) {
            for (int i = 0; i < quantity; i++) {
                writeLocks[add + i] = 0;
            }
        }

        public void writeSingleRegister(int add, short value, int writeTransactionId)
                throws TransactionUsageException, IndexOutOfBoundsException {
            writeMultiRegister(add, 1, new short[] { value }, writeTransactionId);
        }

        public void writeMultiRegister(int add, int quantity, short[] value, int writeTransactionId)
                throws TransactionUsageException, IndexOutOfBoundsException {
            if (add < 0 || quantity <= 0 || add + quantity > holdingRegisters.length) {
                throw new IndexOutOfBoundsException();
            }
            int transactionId = checkLock(add, quantity, writeTransactionId);
            if (transactionId != -1) {
                throw new TransactionUsageException("address is locked by transactionId " + transactionId);
            }
            synchronized (lock) {
                setWriteLock(add, quantity, writeTransactionId);
                for (int i = 0; i < quantity; i++) {
                    if (i < value.length) {
                        holdingRegisters[add + i] = value[i];
                    } else {
                        holdingRegisters[add + i] = 0;
                    }
                }
                unlockWriteLock(add, quantity);
            }
        }
    }

}
