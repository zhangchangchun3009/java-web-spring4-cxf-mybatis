package pers.zcc.scm.web.tcp.client.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.domain.OrderVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pers.zcc.scm.common.util.JacksonUtil;
import pers.zcc.scm.common.vo.Response;
import pers.zcc.scm.web.tcp.client.NettyTcpClient;
import pers.zcc.scm.web.tcp.client.interfaces.ITcpClientReceiver;
import pers.zcc.scm.web.tcp.protocol.TcpCommandEnums;

@Named
public class TcpClientReceiver implements ITcpClientReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpClientReceiver.class);

    @Inject
    private NettyTcpClient nettyTcpClient;

    private ObjectMapper mapper = JacksonUtil.getObjectMapper();

    @Override
    public void read(int command, String messageId, String json) {
        TcpCommandEnums cmd = TcpCommandEnums.getEnumByValue(command);
        switch (cmd) {
        case READ_DEMO:
            try {
                mapper.readValue(json, new TypeReference<Response<OrderVO>>() {
                });
            } catch (Exception e) {
                LOGGER.error("READ_DEMO e", e);
            }
            break;
        case RW_DEMO:
            try {
                Response<OrderVO> response = mapper.readValue(json, new TypeReference<Response<OrderVO>>() {
                });
                nettyTcpClient.writeCommand(command, messageId, response);
            } catch (Exception e) {
                LOGGER.error("RW_DEMO e", e);
            }
            break;
        default:
            LOGGER.info("接收到服务端的数据: " + json);
            break;
        }
    }
}
