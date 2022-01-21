package pers.zcc.scm.common.socket.chatsocket.service.interfaces;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.fasterxml.jackson.databind.JsonNode;

import pers.zcc.scm.common.user.vo.UserVO;
import pers.zcc.scm.common.util.JacksonUtil;
import pers.zcc.scm.common.vo.Response;

public class SocketClient {
    protected static Socket socket;

    protected static int handle;

    protected static UserVO user;

    public static int getHandle() {
        return handle;
    }

    public static void init(String hostName, int hostPort) {
        try {
            socket = new Socket(InetAddress.getByName(hostName), hostPort);
            socket.setReuseAddress(true);
            handle = -1;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void connect(String userName, ListenCallBack callBack) {
        String requestMsg;
        try {
            UserVO register = new UserVO();
            register.setUserName(userName);
            requestMsg = JacksonUtil.getObjectMapper().writeValueAsString(
                    new Response<UserVO>().fail(SocketCodeEnum.LISTEN_START_CODE.getCode(), "请求连接监听", register));
            new DataOutputStream(socket.getOutputStream()).writeUTF(requestMsg);
            listen(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void listen(ListenCallBack callBack) {
        new Thread(() -> {
            while (true) {
                String msg = getMessage(socket);
                if (msg == null || "".equals(msg)) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                try {
                    JsonNode root = JacksonUtil.getObjectMapper().readTree(msg);
                    String code = root.get("code").asText();
                    String data = root.get("data").toString();
                    if (SocketCodeEnum.LISTEN_START_CODE.getCode().equals(code)) {
                        user = JacksonUtil.getObjectMapper().readValue(data, UserVO.class);
                        handle = user.getId();
                    }
                    if (SocketCodeEnum.LISTEN_STOP_CODE.getCode().equals(code)) {
                        return;
                    }
                    if (SocketCodeEnum.CONMUNICATION_CODE.getCode().equals(code)) {
                        callBack.onAccept(socket, data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "t-listenServer").start();
    }

    public static void send(String msg) {
        try {
            String requestMsg = JacksonUtil.getObjectMapper().writeValueAsString(
                    new Response<String>().fail(SocketCodeEnum.CONMUNICATION_CODE.getCode(), "发送通信消息", msg));
            new DataOutputStream(socket.getOutputStream()).writeUTF(requestMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        try {
            String requestMsg = JacksonUtil.getObjectMapper().writeValueAsString(new Response<String>()
                    .fail(SocketCodeEnum.LISTEN_STOP_CODE.getCode(), "请求停止监听", String.valueOf(handle)));
            new DataOutputStream(socket.getOutputStream()).writeUTF(requestMsg);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getMessage(Socket socket) {
        try {
            return new DataInputStream(socket.getInputStream()).readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        init("192.168.3.75", 9001);
        connect("zcc", new ListenCallBack() {
            @Override
            public void onAccept(Socket socket, String message) {
                System.out.println(message);
            }
        });
        send("hello word");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        send("i'm fine");
        close();
    }

}
