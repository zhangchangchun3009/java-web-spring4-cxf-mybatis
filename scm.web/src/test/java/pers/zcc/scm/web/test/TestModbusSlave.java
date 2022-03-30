package pers.zcc.scm.web.test;

import java.util.concurrent.ExecutionException;

import com.digitalpetri.modbus.master.ModbusTcpMaster;

import pers.zcc.scm.common.util.modbustcp.master.ModbusMasterTcpUtil;
import pers.zcc.scm.common.util.modbustcp.slave.server.ModbusSlaveTcpServer;

public class TestModbusSlave {

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "dev");
        testOne();
//        testMulti();
    }

    static void testOne() {
        ModbusTcpMaster master = null;
        try {
            ModbusSlaveTcpServer server = new ModbusSlaveTcpServer();
            server.start();
//            ModbusMasterTcpUtil.WriteSingleRegisterRequest(master, 0, 8, 1);
            master = ModbusMasterTcpUtil.getMasterInstance("127.0.0.1", 502);
            System.out.println(ModbusMasterTcpUtil.readSingleHoldingRegisters(master, 0, 1));
            ModbusMasterTcpUtil.WriteSingleRegisterRequest(master, 0, 4, 1);
//            ModbusMasterTcpUtil.WriteSingleRegisterRequest(master, 0, 4, 2);
//        ModbusPoolDBMS.stop("1");
//        int value = ModbusMasterTcpUtil.readSingleHoldingRegisters(master, 0, 1);
//        System.out.println(value);
//        ModbusPoolDBMS.start("1");
            System.out.println(ModbusMasterTcpUtil.readSingleHoldingRegisters(master, 0, 1));
//            server.shutdown();
            Thread.sleep(60000);
//        System.out.println(ModbusMasterTcpUtil.readSingleHoldingRegisters(master, 0, 1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (master != null) {
                ModbusMasterTcpUtil.release(master);
            }
        }
    }

    static void testMulti() {
        ModbusTcpMaster master = null;
        try {
            ModbusSlaveTcpServer server = new ModbusSlaveTcpServer.Builder().bindHostAddress("127.0.0.1").port(502)
                    .serverId((short) 1).addUnit(1, 64).addUnit(2, 64).build();
            server.start();
            master = ModbusMasterTcpUtil.getMasterInstance("127.0.0.1", 502);
            ModbusMasterTcpUtil.WriteSingleRegisterRequest(master, 0, 10, 1);
            System.out.println(ModbusMasterTcpUtil.readSingleHoldingRegisters(master, 0, 1));
            ModbusMasterTcpUtil.WriteSingleRegisterRequest(master, 0, 7, 2);
            System.out.println(ModbusMasterTcpUtil.readSingleHoldingRegisters(master, 0, 2));
//            ModbusPoolDBMS.stop("2");
//            System.out.println(ModbusMasterTcpUtil.readSingleHoldingRegisters(master, 0, 2));
//            server.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (master != null) {
                ModbusMasterTcpUtil.release(master);
            }
        }
    }

}
