package pers.zcc.scm.web.start;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
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

    public static void main(String[] args) {
        Tomcat tomcat = new Tomcat();
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

}
