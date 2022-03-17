package pers.zcc.scm.web.start;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pers.zcc.scm.common.util.EnvironmentProps;

/**
 * 嵌入式tomcat启动类
 * @author zhangchangchun
 * @Date 2022年2月19日
 */
public class AppBootstrap {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppBootstrap.class);

    static Tomcat tomcat;

    /**
     * 另一种外部关闭方式是在服务器的8005 socket端口写入"SHUTDOWN"信息
     * @throws InterruptedException
     */
    public static void stop() throws InterruptedException {
        String stopFilePath = SystemUtils.IS_OS_WINDOWS ? "D:\\stopscm.txt" : "/usr/zcc/stopscm.txt";
        File stopFile = new File(stopFilePath);
        while (true) {
            if (stopFile.exists()) {
                try {
                    innerStop();
                } finally {
                    stopFile.delete();
                }
                break;
            }
            Thread.sleep(1000);
        }
    }

    private static void innerStop() {
        if (tomcat == null || tomcat.getServer().getState() != LifecycleState.STARTED) {
            return;
        }
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            LOGGER.error("tomcat stop server occured LifecycleException,", e);
        }
    }

    public static void start() {
        tomcat = new Tomcat();
        String springProfilesActive = System.getProperty("spring.profiles.active");
        String baseDir = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        File passfile = new File(baseDir + "conf/" + springProfilesActive + "/cert/jks-password.txt");
        String jksPassword = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(passfile))) {
            jksPassword = reader.readLine();
        } catch (FileNotFoundException e) {
            LOGGER.error("tomcat start failed:", e);
        } catch (IOException e) {
            LOGGER.error("tomcat start failed:", e);
        }
        String appPropPath = baseDir + "conf/" + springProfilesActive + "/application.properties";
        int port = EnvironmentProps.getInteger(appPropPath, "server.port", 8080);
        int sslPort = EnvironmentProps.getInteger(appPropPath, "server.sslPort", 8443);
        int shutdownPort = EnvironmentProps.getInteger(appPropPath, "server.shutdownPort", 8005);
        String shutdownCmd = EnvironmentProps.getString(appPropPath, "server.shutdownCmd", "SHUTDOWN");
        String contextPath = EnvironmentProps.getString(appPropPath, "server.contextPath", "/scm.web");
        String maxThread = EnvironmentProps.getString(appPropPath, "server.maxThread", "200");

        long requestTimeoutMs = EnvironmentProps.getLong(appPropPath, "server.requestTimeoutMs", 120000L);
        LOGGER.info("tomcat start at port:" + port);
        LOGGER.info("tomcat start at baseDir:" + baseDir);
        LOGGER.info("tomcat start at contextPath:" + contextPath);
        tomcat.setBaseDir(baseDir);
        tomcat.setPort(port);
        try {
            tomcat.addWebapp(contextPath, baseDir);
        } catch (ServletException e) {
            LOGGER.error("tomcat start failed:", e);
        }
        tomcat.enableNaming();
        Server server = tomcat.getServer();
        server.setPort(shutdownPort);
        server.setShutdown(shutdownCmd);
        Service service = tomcat.getService();
        Connector httpConnector = new Connector();
        httpConnector.setPort(port);
        httpConnector.setRedirectPort(sslPort);
        httpConnector.setAsyncTimeout(requestTimeoutMs);
        httpConnector.setProtocol("HTTP/1.1");
        httpConnector.setScheme("http");
        httpConnector.setProperty("maxThreads", maxThread);
        tomcat.setConnector(httpConnector);
        service.addConnector(httpConnector);

        Connector sslConnector = new Connector();
        sslConnector.setPort(sslPort);
        sslConnector.setAsyncTimeout(requestTimeoutMs);
        sslConnector.setProtocol("org.apache.coyote.http11.Http11NioProtocol");
        sslConnector.setScheme("https");
        sslConnector.setProperty("maxThreads", maxThread);
        sslConnector.setProperty("SSLEnabled", "true");
        sslConnector.setProperty("secure", "true");
        sslConnector.setProperty("clientAuth", "false");
        sslConnector.setProperty("sslProtocol", "TLS");
        sslConnector.setProperty("keystoreFile", "./conf/" + springProfilesActive + "/cert/tomcat.jks");
        sslConnector.setProperty("keystorePass", jksPassword);
        service.addConnector(sslConnector);
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            LOGGER.error("tomcat start failed:", e);
        }
        server.await();
        innerStop();
    }

    public static void main(String[] args) {
        new Thread(() -> {
            start();
        }, "main-start").start();
        Thread stopThread = new Thread(() -> {
            try {
                stop();
            } catch (InterruptedException e) {
                return;
            }
        }, "main-stop");
        stopThread.setDaemon(true);
        stopThread.start();
    }

}
