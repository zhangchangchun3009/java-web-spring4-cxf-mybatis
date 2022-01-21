package pers.zcc.scm.common.socket.chatsocket.service.impl;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pers.zcc.scm.common.socket.chatsocket.service.interfaces.ChatCmdEnum;
import pers.zcc.scm.common.socket.chatsocket.service.interfaces.ChatMessage;
import pers.zcc.scm.common.socket.chatsocket.service.interfaces.ListenCallBack;
import pers.zcc.scm.common.socket.chatsocket.service.interfaces.SocketClient;
import pers.zcc.scm.common.util.JacksonUtil;

public class ChatClient extends SocketClient {

    private static List<String> friendsList;

    private static String chattingFriend;

    private static Scanner scanner;

    private static CmdObserverNotifier notifier;

    private static ObjectMapper mapper;

    private static String content;

    private static boolean chatCmdLock = true;

    private static void initThis() {
        scanner = new Scanner(System.in);
        notifier = new CmdObserverNotifier();
        ChatCmdObserver observer = new ChatCmdObserver();
        notifier.addObserver(observer);
        mapper = JacksonUtil.getObjectMapper();
    }

    public static void main(String[] args) {
        initThis();

        System.out.println("===开启客户端===");
        System.out.println("===交互说明：按enter键结束输入，多个输入以空格分割===");
//        System.out.println("输入登录服务器主机和端口：");
//        String hostName = scanner.next();
//        int hostPort = scanner.nextInt();
        String hostName = "192.168.3.75";
        int hostPort = 9002;
        SocketClient.init(hostName, hostPort);
        System.out.println("===连接成功===");

        System.out.println("===输入注册用户名：===");
        String userName = scanner.nextLine();
        connect(userName, new ListenCallBack() {
            @Override
            public void onAccept(Socket socket, String message) {
                try {
                    ChatMessage response = mapper.readValue(message, ChatMessage.class);
                    System.out.println("收到好友消息：" + response.toString());
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        setFriendsList(getFriendsFromServer());
        System.out.println("===注册成功，输入':Q'可退出客户端===");

        System.out.println("===选择输入在线好友账号：===");
        chattingFriend = getAnyInput();

        System.out.println("===可以开始聊天了===");
        while (true) {
            chat();
        }
    }

    public static void chat() {
        chatCmdLock = false;
        System.out.println("===输入发送消息内容：(输入':C'切换在线聊天好友账号)===");
        String input = getInput();
        if (input != null) {
            content = input;
            System.out.println("===输入':S'发送消息：(输入':E'可重新编写消息内容)===");
            getInput();
        }
    }

    public static void sendMessage() {
        if (content == null) {
            System.out.println("还没有输入消息内容");
            return;
        }
        ChatMessage message = new ChatMessage(ChatClient.user.getUserName(), chattingFriend, content, getDateNow());
        try {
            String sendMsg = mapper.writeValueAsString(message);
            send(sendMsg);
            content = null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getFriendsFromServer() {
        return Arrays.asList("zcc", "ff", "hh");
    }

    public static String getInput() {
        String input = scanner.nextLine();
        ChatCmdEnum cmd = ChatCmdEnum.findCmdByValue(input);
        if (cmd != null) {
            notifier.setChanged();
            notifier.notifyObservers(cmd);
            return null;
        } else {
            return input;
        }
    }

    public static String getAnyInput() {
        return scanner.nextLine();
    }

    public static boolean hasInput() {
        return scanner.hasNext();
    }

    public static String getDateNow() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * @return the friendsList
     */
    public static List<String> getFriendsList() {
        return friendsList;
    }

    /**
     * @param friendsList the friendsList to set
     */
    public static void setFriendsList(List<String> friendsList) {
        ChatClient.friendsList = friendsList;
    }

    public static String getChattingFriend() {
        return chattingFriend;
    }

    public static void setChattingFriend(String chattingFriend) {
        ChatClient.chattingFriend = chattingFriend;
    }

    public static String getContent() {
        return content;
    }

    public static void setContent(String content) {
        ChatClient.content = content;
    }

    public static boolean getChatCmdLock() {
        return chatCmdLock;
    }

    public static void setChatCmdLock(boolean chatCmdLock) {
        ChatClient.chatCmdLock = chatCmdLock;
    }

}
