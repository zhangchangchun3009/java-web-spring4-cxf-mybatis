package pers.zcc.scm.common.util.modbustcp.slave.server;

import javax.inject.Named;

import pers.zcc.scm.common.frame.IBeforeShutDownHandle;

@Named
public class ModbusSlaveTcpReleaser implements IBeforeShutDownHandle {

    @Override
    public void process() {
        for (ModbusSlaveTcpServer slave : ModbusSlaveInstanceRegister.getInstances()) {
            slave.shutdown();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
