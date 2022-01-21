package pers.zcc.scm.common.socket.chatsocket.service.impl;

import pers.zcc.scm.common.socket.chatsocket.service.interfaces.ICommandHandler;

public class EHandler implements ICommandHandler {

    @Override
    public void handle() {
        if (ChatClient.getChatCmdLock()) {
            return;
        }
        System.out.println("请输入要发送内容：");
        String content = ChatClient.getAnyInput();
        ChatClient.setContent(content);
        System.out.println("===输入':S'发送消息：(输入':E'可重新编写消息内容)===");
        ChatClient.getInput();
    }

}
