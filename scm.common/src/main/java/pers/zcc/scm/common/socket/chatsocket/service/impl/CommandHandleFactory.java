package pers.zcc.scm.common.socket.chatsocket.service.impl;

import pers.zcc.scm.common.socket.chatsocket.service.interfaces.ChatCmdEnum;
import pers.zcc.scm.common.socket.chatsocket.service.interfaces.ICommandHandler;

public class CommandHandleFactory {
    public static ICommandHandler getInstance(ChatCmdEnum cmd) {
        switch (cmd) {
        case CHANGE_FRIEND:
            return new CHandler();
        case EDIT:
            return new EHandler();
        case SEND:
            return new SHandler();
        case QUIT:
            return new QHandler();
        default:
            return null;
        }
    }
}
