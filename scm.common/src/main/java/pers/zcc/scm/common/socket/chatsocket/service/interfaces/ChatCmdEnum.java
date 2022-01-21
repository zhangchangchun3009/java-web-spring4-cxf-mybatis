package pers.zcc.scm.common.socket.chatsocket.service.interfaces;

public enum ChatCmdEnum {
    QUIT(":Q"), EDIT(":E"), CHANGE_FRIEND(":C"), SEND(":S");

    private ChatCmdEnum(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    private String value;

    public static ChatCmdEnum findCmdByValue(String value) {
        for (ChatCmdEnum ele : ChatCmdEnum.values()) {
            if (ele.value.equals(value)) {
                return ele;
            }
        }
        return null;
    }

}
