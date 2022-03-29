package pers.zcc.scm.common.util.modbustcp.slave.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;

import pers.zcc.scm.common.frame.IAfterStartUpHandle;
import pers.zcc.scm.common.util.modbustcp.slave.server.ModbusSlaveTcpServer.Builder;

/**
 * The Class ModbusSlaveTcpStarter.
 * @author zcc
 * @since 2021年12月13日
 */
@Named
public class ModbusSlaveTcpStarter implements IAfterStartUpHandle {

    @Value("${scm.common.modbusslave.bindIp}")
    private String ip;

    @Value("${scm.common.modbusslave.port}")
    private int port;

    @Value("${scm.common.modbusslave.serverId}")
    private short serverId;

    @Override
    public void process() {
        List<Map<String, Object>> plcList = new ArrayList<Map<String, Object>>(); //master list from configuration,may from db
        ModbusSlaveTcpServer.Builder builder = new Builder();
        builder.bindHostAddress(ip).port(port).serverId(serverId);
        for (Map<String, Object> plc : plcList) {
            int poolSize = (int) plc.get("poolSize");
            int unitId = (int) plc.get("unitId");
            builder.addUnit(unitId, poolSize);
        }
        ModbusSlaveTcpServer slave = builder.build();
        ModbusSlaveInstanceRegister.regist(serverId, slave);
        slave.start();
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
