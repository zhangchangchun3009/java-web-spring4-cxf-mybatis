package pers.zcc.scm.web.tcp.client.impl;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;

@Named
public class TcpServerConfig {
    @Value("${tcp.server.host:127.0.0.1}")
    private String host;

    @Value("${tcp.server.port:8030}")
    private int port;

    @Value("${application.appId}")
    private String unitId;

    @Value("${tcp.password}")
    private String password;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

}
