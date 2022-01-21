
package pers.zcc.scm.common.util.modbustcp.slave.server;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digitalpetri.modbus.codec.Modbus;
import com.digitalpetri.modbus.slave.ModbusTcpSlave;
import com.digitalpetri.modbus.slave.ModbusTcpSlaveConfig;

import pers.zcc.scm.common.util.modbustcp.slave.db.ModbusPoolDBMS;
import pers.zcc.scm.common.util.modbustcp.slave.requesthandler.HoldingRegisterRequestHandle;

/**
 * The Class ModbusSlaveTcpServer.
 *
 * @author zcc
 * @since 2021年12月13日
 */
public class ModbusSlaveTcpServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModbusSlaveTcpServer.class);

    private ModbusTcpSlave instance;

    private int port;

    private int poolSize;

    private short instanceId;

    public short getInstanceId() {
        return instanceId;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public int getPort() {
        return port;
    }

    public ModbusSlaveTcpServer() {
        this(502, 256, (short) 1);
    }

    public ModbusSlaveTcpServer(int port, int poolSize, short instanceId) {
        this.port = port;
        this.poolSize = poolSize;
        this.instanceId = instanceId;
        ModbusPoolDBMS.createInstance(String.valueOf(instanceId), poolSize);
    }

    public void start() {
        ModbusTcpSlaveConfig config = new ModbusTcpSlaveConfig.Builder().setInstanceId(String.valueOf(instanceId))
                .build();
        instance = new ModbusTcpSlave(config);
        instance.setRequestHandler(new HoldingRegisterRequestHandle());
        CompletableFuture<ModbusTcpSlave> future = instance.bind("127.0.0.1", port);
        try {
            future.get();
            LOGGER.info("modbus slave service start success, port:" + port);
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("ModbusSlaveTcpServer start failed", e);
        }
    }

    public void shutdown() {
        instance.shutdown();
        Modbus.releaseSharedResources();
    }

}
