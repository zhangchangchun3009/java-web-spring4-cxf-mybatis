package pers.zcc.scm.web.tcp.server.impl;

import javax.inject.Inject;
import javax.inject.Named;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pers.zcc.scm.common.util.JacksonUtil;
import pers.zcc.scm.web.tcp.protocol.LoginCommand;
import pers.zcc.scm.web.tcp.server.interfaces.ITcpServerReceiver;

@Named
public class TcpServerReceiver implements ITcpServerReceiver {
    ObjectMapper mapper = JacksonUtil.getObjectMapper();

    @Inject
    WriteCommandService writeService;

    @Inject
    MessageCacheService cacheService;

    @Override
    public boolean login(String unitId, String json) {
        try {
            LoginCommand loginCommand = mapper.readValue(json, LoginCommand.class);
            if (loginCommand == null)
                return false;
//            String key = loginCommand.getKey();
            String password = loginCommand.getPassword();
            if ("123".equals(password)) {
                System.out.println("login...........ok.");
                return true;
            } else
                return false;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void read(String unitId, int command, String messageId, String json) {
        if (null == messageId || messageId.equals("")) {
            //不需要返回的消息
        } else {
            //以message为key，将json对象存入缓存中
            cacheService.putMessage(messageId, json);
        }
    }
}
