package pers.zcc.scm.web.start;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
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
        int port = Integer.parseInt(EnvironmentProps.getProperty(appPropPath, "server.port"));
        int sslPort = Integer.parseInt(EnvironmentProps.getProperty(appPropPath, "server.sslPort"));
        String contextPath = EnvironmentProps.getProperty(appPropPath, "server.contextPath");
        String maxThread = EnvironmentProps.getProperty(appPropPath, "server.maxThread");
        long requestTimeoutMs = Long.parseLong(EnvironmentProps.getProperty(appPropPath, "server.requestTimeoutMs"));
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
        Connector httpConnector = new Connector();
        httpConnector.setPort(port);
        httpConnector.setRedirectPort(sslPort);
        httpConnector.setAsyncTimeout(requestTimeoutMs);
        httpConnector.setProtocol("HTTP/1.1");
        httpConnector.setScheme("http");
        httpConnector.setProperty("maxThreads", maxThread);
        tomcat.setConnector(httpConnector);
        tomcat.getService().addConnector(httpConnector);

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
        tomcat.getService().addConnector(sslConnector);
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            LOGGER.error("tomcat start failed:", e);
        }
        tomcat.getServer().await();
    }

    public static void main(String[] args) {
        new Thread(() -> {
            start();
        }).start();
        new Thread(() -> {
            try {
                stop();
            } catch (InterruptedException e) {
                return;
            }
        }).start();
    }

}
