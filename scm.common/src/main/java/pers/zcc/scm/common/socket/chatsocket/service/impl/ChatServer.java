package pers.zcc.scm.common.socket.chatsocket.service.impl;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pers.zcc.scm.common.socket.chatsocket.service.interfaces.ChatMessage;
import pers.zcc.scm.common.socket.chatsocket.service.interfaces.ListenCallBack;
import pers.zcc.scm.common.socket.chatsocket.service.interfaces.SocketCodeEnum;
import pers.zcc.scm.common.socket.chatsocket.service.interfaces.SocketServer;
import pers.zcc.scm.common.user.vo.UserVO;
import pers.zcc.scm.common.util.JacksonUtil;
import pers.zcc.scm.common.vo.Response;

public class ChatServer extends SocketServer {

    private static final ChatServer INSTANCE = new ChatServer(9002, 100);

    private ChatServer(int port, int backlog) {
        super(port, backlog);
    }

    public static ChatServer getInstance() {
        return INSTANCE;
    }

    public static void start() {
        INSTANCE.startListen(new ListenCallBack() {
            @Override
            public void onAccept(Socket fromSocket, String message) {
                try {
                    ObjectMapper mapper = JacksonUtil.getObjectMapper();
                    ChatMessage chtMessageVo = mapper.readValue(message, ChatMessage.class);
                    String to = chtMessageVo.getTo();
                    String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    UserVO toUser = INSTANCE.userRegister.get(to);
                    boolean toOnline = true;
                    Socket toSocket = null;
                    if (toUser == null) {
                        toOnline = false;
                    } else {
                        toSocket = INSTANCE.connections.get(toUser.getId());
                        if (toSocket == null || toSocket.isClosed()) {
                            toOnline = false;
                        }
                    }
                    if (!toOnline) {
                        ChatMessage response = new ChatMessage();
                        response.setFrom("Server");
                        response.setTo(chtMessageVo.getFrom());
                        response.setSendTime(now);
                        response.setMessageBody("您的好友-" + to + "-当前不在线");
                        SocketServer.writeMessage(fromSocket, mapper.writeValueAsString(new Response<ChatMessage>()
                                .fail(SocketCodeEnum.CONMUNICATION_CODE.getCode(), "服务端通知消息", response)));
                    } else {
                        SocketServer.writeMessage(toSocket, mapper.writeValueAsString(new Response<ChatMessage>()
                                .fail(SocketCodeEnum.CONMUNICATION_CODE.getCode(), "服务端转发用户消息", chtMessageVo)));
                    }
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void stop() {
        INSTANCE.close();
    }

    public static void main(String[] args) {
        ChatServer.start();
    }

}
