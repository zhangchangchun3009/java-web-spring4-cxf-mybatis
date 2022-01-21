
package pers.zcc.scm.common.util.modbustcp.slave.server;

import javax.inject.Named;

import pers.zcc.scm.common.frame.IAfterStartUpHandle;

/**
 * The Class ModbusSlaveTcpStarter.
 * @author zcc
 * @since 2021年12月13日
 */
@Named
public class ModbusSlaveTcpStarter implements IAfterStartUpHandle {

    @Override
    public void process() {
        ModbusSlaveTcpServer slave = new ModbusSlaveTcpServer();
        ModbusSlaveInstanceRegister.regist(slave.getInstanceId(), slave);
        slave.start();
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
