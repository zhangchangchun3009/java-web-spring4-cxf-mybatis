package pers.zcc.scm.common.socket.chatsocket.service.impl;

import pers.zcc.scm.common.socket.chatsocket.service.interfaces.ICommandHandler;

public class SHandler implements ICommandHandler {

    @Override
    public void handle() {
        if (ChatClient.getChatCmdLock()) {
            return;
        }
        ChatClient.sendMessage();
    }

}
