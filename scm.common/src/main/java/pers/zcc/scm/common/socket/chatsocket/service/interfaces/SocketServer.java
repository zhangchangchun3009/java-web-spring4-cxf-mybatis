package pers.zcc.scm.common.socket.chatsocket.service.interfaces;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.JsonNode;

import pers.zcc.scm.common.user.vo.UserVO;
import pers.zcc.scm.common.util.JacksonUtil;
import pers.zcc.scm.common.util.UUIDHexGenerator;
import pers.zcc.scm.common.vo.Response;

public class SocketServer {

    protected int backlog;

    protected int port;

    protected final AtomicInteger handleCnt = new AtomicInteger(0);

    protected ServerSocket serverSocket;

    protected final ConcurrentHashMap<Integer, Socket> connections = new ConcurrentHashMap<>();

    protected final ConcurrentHashMap<String, UserVO> userRegister = new ConcurrentHashMap<>();

    public int getBacklog() {
        return backlog;
    }

    public int getPort() {
        return port;
    }

    public SocketServer(int port, int backlog) {
        if (!init(port, backlog)) {
            throw new IllegalStateException();
        }
        System.out.println("服务器启动成功，端口：" + port);
    }

    private boolean init(int port, int backlog) {
        try {
            this.port = port;
            this.backlog = backlog;
            serverSocket = new ServerSocket(port, backlog, InetAddress.getLocalHost());
            serverSocket.setReuseAddress(true);
            try {
                serverSocket.setSoTimeout(0);
            } catch (SocketException e1) {
                e1.printStackTrace();
            }
            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void startListen(ListenCallBack callBack) {
        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                String requestMsg = getMessage(socket);
                JsonNode root = JacksonUtil.getObjectMapper().readTree(requestMsg);
                JsonNode codeN = root.get("code");
                JsonNode dataN = root.get("data");
                if (codeN != null && dataN != null) {
                    String code = codeN.asText();
                    if (SocketCodeEnum.LISTEN_START_CODE.getCode().equals(code)) {
                        int handle = handleCnt.addAndGet(1);
                        if (handle > backlog) {
                            String responseMsg = JacksonUtil.getObjectMapper()
                                    .writeValueAsString(new Response<String>().fail("999", "已到达连接上限"));
                            writeMessage(socket, responseMsg);
                            continue;
                        }
                        JsonNode nameN = dataN.get("userName");
                        if (nameN == null) {
                            String responseMsg = JacksonUtil.getObjectMapper()
                                    .writeValueAsString(new Response<String>().fail("998", "注册用户名为空"));
                            writeMessage(socket, responseMsg);
                            continue;
                        }
                        UserVO user = new UserVO();
                        user.setUserName(nameN.asText());
                        user.setUserId(UUIDHexGenerator.toInt(new UUIDHexGenerator().generate().getBytes("utf-8")));
                        user.setId(handle);
                        userRegister.put(user.getUserName(), user);
                        connections.put(handle, socket);
                        System.out.println("新增连接成功，用户名：" + user.getUserName() + ",分配临时id：" + handle);
                        String responseMsg = JacksonUtil.getObjectMapper().writeValueAsString(
                                new Response<UserVO>().fail(SocketCodeEnum.LISTEN_START_CODE.getCode(), "连接成功", user));
                        writeMessage(socket, responseMsg);
                        listen(callBack, socket, handle);
                    } else {
                        System.out.println("...." + code);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void writeMessage(Socket socket, String responseMsg) throws IOException {
        new DataOutputStream(socket.getOutputStream()).writeUTF(responseMsg);
    }

    private void listen(ListenCallBack callBack, Socket socket, int handle) {
        new Thread(() -> {
            while (true) {
                if (socket.isClosed() || !socket.isConnected() || socket.isInputShutdown()
                        || socket.isOutputShutdown()) {
                    return;
                }
                String msg = "";
                try {
                    msg = getMessage(socket);
                } catch (IOException e) {
                    stopListen(handle);
                    return;
                }
                System.out.println("收到客户端消息：" + msg + ",用户句柄:" + handle);
                InetSocketAddress remoteHost = (InetSocketAddress) socket.getRemoteSocketAddress();
                String clientIp = remoteHost.getAddress().getHostAddress();
                int clientPort = remoteHost.getPort();
                System.out.println("用户IP:" + clientIp);
                System.out.println("用户连接使用端口:" + clientPort);
                if (msg == null || "".equals(msg)) {
                    continue;
                }
                try {
                    JsonNode root = JacksonUtil.getObjectMapper().readTree(msg);
                    String code = root.get("code").asText();
                    String data = root.get("data").asText();
                    if (code == null) {
                        continue;
                    }
                    if (SocketCodeEnum.LISTEN_STOP_CODE.getCode().equals(code)) {
                        int reqhandle = Integer.parseInt(data);
                        stopListen(reqhandle);
                        return;
                    }
                    if (SocketCodeEnum.CONMUNICATION_CODE.getCode().equals(code)) {
                        callBack.onAccept(socket, data);
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }, "t-listen-" + handle).start();
    }

    public static String getMessage(Socket socket) throws IOException {
        return new DataInputStream(socket.getInputStream()).readUTF();
    }

    public boolean stopListen(int handle) {
        Socket socket = connections.get(handle);
        connections.remove(handle);
        if (socket != null && socket.isConnected()) {
            try {
                socket.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SocketServer server = new SocketServer(9001, 100);
        server.startListen(new ListenCallBack() {
            @Override
            public void onAccept(Socket socket, String message) {
                InetSocketAddress remoteHost = (InetSocketAddress) socket.getRemoteSocketAddress();
                String clientIp = remoteHost.getAddress().getHostAddress();
                int clientPort = remoteHost.getPort();
                System.out.println("clientIp:" + clientIp);
                System.out.println("clientPort:" + clientPort);
                System.out.println("message:" + message);
            }
        });
    }

}
