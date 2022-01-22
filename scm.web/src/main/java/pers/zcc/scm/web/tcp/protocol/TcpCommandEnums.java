package pers.zcc.scm.web.tcp.protocol;

public enum TcpCommandEnums {
    LOGIN(0, "登录或登录成功"), READ_DEMO(1, "只读测试"), RW_DEMO(1, "读写测试");

    TcpCommandEnums(int value, String name) {
        this.value = value;
        this.name = name;
    }

    int value;

    String name;

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static TcpCommandEnums getEnumByValue(int value) {
        for (TcpCommandEnums ele : TcpCommandEnums.values()) {
            if (ele.getValue() == value) {
                return ele;
            }
        }
        return null;
    }

}