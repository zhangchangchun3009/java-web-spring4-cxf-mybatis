package pers.zcc.scm.common.socket.chatsocket.service.interfaces;

public class ChatMessage {
    String from;

    String to;

    String messageBody;

    String sendTime;

    public ChatMessage() {
    }

    public ChatMessage(String from, String to, String messageBody, String sendTime) {
        super();
        this.from = from;
        this.to = to;
        this.messageBody = messageBody;
        this.sendTime = sendTime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "[from=" + from + ", to=" + to + ", sendTime=" + sendTime + "] " + messageBody;
    }

}
