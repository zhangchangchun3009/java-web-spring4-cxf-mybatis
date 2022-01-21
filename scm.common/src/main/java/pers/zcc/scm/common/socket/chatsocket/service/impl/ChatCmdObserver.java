package pers.zcc.scm.common.socket.chatsocket.service.impl;

import java.util.Observable;
import java.util.Observer;

import pers.zcc.scm.common.socket.chatsocket.service.interfaces.ChatCmdEnum;
import pers.zcc.scm.common.socket.chatsocket.service.interfaces.ICommandHandler;

public class ChatCmdObserver implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        ChatCmdEnum cmd = (ChatCmdEnum) arg;
        ICommandHandler cmdHandler = CommandHandleFactory.getInstance(cmd);
        cmdHandler.handle();
    }

}
