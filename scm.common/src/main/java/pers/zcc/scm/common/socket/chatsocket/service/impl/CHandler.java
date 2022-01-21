package pers.zcc.scm.common.socket.chatsocket.service.impl;

import pers.zcc.scm.common.socket.chatsocket.service.interfaces.ICommandHandler;

public class CHandler implements ICommandHandler {

    @Override
    public void handle() {
        if (ChatClient.getChatCmdLock()) {
            return;
        }
        System.out.println("===选择输入在线好友账号：===");
        String chattingFriend = ChatClient.getAnyInput();
        ChatClient.setChattingFriend(chattingFriend);
        ChatClient.setContent(null);
        System.out.println("===切换好友账号到-" + chattingFriend + "-成功===");
    }

}
