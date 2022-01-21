package pers.zcc.scm.common.socket.chatsocket.service.impl;

import java.util.concurrent.atomic.AtomicBoolean;

import pers.zcc.scm.common.socket.chatsocket.service.interfaces.ICommandHandler;

public class QHandler implements ICommandHandler {

    private AtomicBoolean cancel = new AtomicBoolean(false);

    @Override
    public void handle() {
        System.out.println("===您输入了退出命令,3S后系统将退出(输入任何字符可取消退出)===");
        Thread t = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    if (ChatClient.hasInput()) {
                        cancel.set(true);
                        ChatClient.getAnyInput();
                        break;
                    }
                }
            } catch (Exception e) {
            }
        });
        t.start();
        int cnt = 0;
        while (cnt < 3) {
            cnt++;
            if (cancel.get()) {
                cancel.set(false);
                t.interrupt();
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ChatClient.close();
        System.out.println("===系统退出===");
        System.exit(-1);
    }

}
