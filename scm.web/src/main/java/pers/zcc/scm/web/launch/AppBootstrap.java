package pers.zcc.scm.web.launch;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatEmbeddedWebappClassLoader;

import pers.zcc.scm.common.util.EnvironmentProps;

/**
 * 嵌入式tomcat启动类
 * @author zhangchangchun
 * @Date 2022年2月19日
 */
public class AppBootstrap {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppBootstrap.class);

    private static Tomcat tomcat;

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

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL bootURL = AppBootstrap.class.getProtectionDomain().getCodeSource().getLocation();
        String baseDir = bootURL.getPath();
        boolean runInjar = baseDir.contains(".jar");

        try {
            baseDir = getAndMakeBaseDir(cl, baseDir, runInjar);
        } catch (URISyntaxException e) {
            LOGGER.error("getAndMakeBaseDir e,", e);
            return;
        }

        String jksPassword = getCertPassword(cl);

        Properties props = getAppProp(cl);

        int port = EnvironmentProps.getInteger(props, "server.port", 8080);
        String contextPath = EnvironmentProps.getString(props, "server.contextPath", "/scm.web");

        LOGGER.info("tomcat start at port:" + port);
        LOGGER.info("tomcat start at baseDir:" + baseDir);
        LOGGER.info("tomcat start at contextPath:" + contextPath);

        tomcat.setBaseDir(baseDir);
        Context context = tomcat.addWebapp(contextPath, baseDir);
        tomcat.enableNaming();

        Host host = tomcat.getHost();
        host.setAutoDeploy(false);

        setContextWebappClassLoader(cl, context);

        Server server = tomcat.getServer();
        setServer(server, props);

        Service service = tomcat.getService();
        Connector httpConnector = configHttpConnector(props);
        service.addConnector(httpConnector);
        Connector sslConnector = configSSLConnector(baseDir, runInjar, jksPassword, props);
        service.addConnector(sslConnector);

        try {
            tomcat.start();
        } catch (LifecycleException e) {
            LOGGER.error("tomcat start failed:", e);
        }

        server.await();

        innerStop();
    }

    private static void setContextWebappClassLoader(ClassLoader cl, Context context) {
        WebappLoader loader = new WebappLoader();
        loader.setLoaderClass(TomcatEmbeddedWebappClassLoader.class.getName());
        loader.setDelegate(true);
        context.setLoader(loader);
        context.setParentClassLoader(cl);
    }

    private static void setServer(Server server, Properties props) {
        int shutdownPort = EnvironmentProps.getInteger(props, "server.shutdownPort", 8005);
        String shutdownCmd = EnvironmentProps.getString(props, "server.shutdownCmd", "SHUTDOWN");
        server.setPort(shutdownPort);
        server.setShutdown(shutdownCmd);
    }

    private static Properties getAppProp(ClassLoader cl) {
        BufferedInputStream propBin = new BufferedInputStream(
                cl.getResourceAsStream("conf/" + EnvironmentProps.getActiveProfile() + "/application.properties"));
        Properties props = EnvironmentProps.getProperties(propBin);
        return props;
    }

    private static Connector configSSLConnector(String baseDir, boolean runInjar, String jksPassword,
            Properties props) {
        Connector sslConnector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        int sslPort = EnvironmentProps.getInteger(props, "server.sslPort", 8443);
        String maxThread = EnvironmentProps.getString(props, "server.maxThread", "200");
        long requestTimeoutMs = EnvironmentProps.getLong(props, "server.requestTimeoutMs", 120000L);
        sslConnector.setPort(sslPort);
        sslConnector.setAsyncTimeout(requestTimeoutMs);
        sslConnector.setScheme("https");
        sslConnector.setProperty("maxThreads", maxThread);
        sslConnector.setProperty("SSLEnabled", "true");
        sslConnector.setProperty("secure", "true");
        sslConnector.setProperty("clientAuth", "false");
        sslConnector.setProperty("sslProtocol", "TLS");
        //when run in jar, gg, dir must be in file system, still i can't get one fat jar to run
        sslConnector.setProperty("keystoreFile", runInjar ? baseDir + "/cert/tomcat.jks"
                : "./conf/" + EnvironmentProps.getActiveProfile() + "/cert/tomcat.jks");
        sslConnector.setProperty("keystorePass", jksPassword);
        return sslConnector;
    }

    private static Connector configHttpConnector(Properties props) {
        Connector httpConnector = new Connector("HTTP/1.1");
        int port = EnvironmentProps.getInteger(props, "server.port", 8080);
        int sslPort = EnvironmentProps.getInteger(props, "server.sslPort", 8443);
        String maxThread = EnvironmentProps.getString(props, "server.maxThread", "200");
        long requestTimeoutMs = EnvironmentProps.getLong(props, "server.requestTimeoutMs", 120000L);
        httpConnector.setPort(port);
        httpConnector.setRedirectPort(sslPort);
        httpConnector.setAsyncTimeout(requestTimeoutMs);
        httpConnector.setScheme("http");
        httpConnector.setProperty("maxThreads", maxThread);
        return httpConnector;
    }

    private static String getCertPassword(ClassLoader cl) {
        String jksPassword = "";
        //change the input stream to fit the way of get resource in a jar
        try (BufferedInputStream bin = new BufferedInputStream(
                cl.getResourceAsStream("conf/" + EnvironmentProps.getActiveProfile() + "/cert/jks-password.txt"));) {
            byte[] buffer = new byte[bin.available()];
            bin.read(buffer);
            jksPassword = new String(buffer, "UTF-8");
        } catch (FileNotFoundException e) {
            LOGGER.error("getCertPassword failed:", e);
        } catch (IOException e) {
            LOGGER.error("getCertPassword failed:", e);
        }
        return jksPassword;
    }

    private static String getAndMakeBaseDir(ClassLoader cl, String baseDir, boolean runInjar)
            throws URISyntaxException {
        if (runInjar) {
            baseDir = baseDir.substring(0, baseDir.indexOf(".jar"));
            baseDir = baseDir.substring(0, baseDir.lastIndexOf("/"));
            /**
             * when pack with spring boot maven plugin, I have to set the webapp classloader 
             * of embedded tomcat by springboot jar classloader. that's difficult, and spring boot has do it.
             * the only easy way to run my app is pack the dependencies out of jar,
             * and jar plugin or assembly plugin is enough to do so
            */
            baseDir = baseDir + "/webapp";
            File webapp;
            webapp = new File(new URI(baseDir));
            baseDir = webapp.getPath();
            if (!webapp.exists()) {
                webapp.mkdir();
            }
            copyCertToTempDir(cl, baseDir);
        } else {
            baseDir = cl.getResource("").getPath(); //when run in jar, gg, dir must be in file system
        }
        return baseDir;
    }

    private static void copyCertToTempDir(ClassLoader cl, String baseDir) {
        String certDir = baseDir + "/cert";
        String certPath = certDir + "/tomcat.jks";
        File certDirF = new File(certDir);
        File cert = new File(certPath);
        if (!certDirF.exists() || !cert.exists()) {
            certDirF.mkdir();
            try (BufferedInputStream bin = new BufferedInputStream(
                    cl.getResourceAsStream("conf/" + EnvironmentProps.getActiveProfile() + "/cert/tomcat.jks"));
                    BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(cert))) {
                byte[] buffer = new byte[bin.available()];
                bin.read(buffer);
                bout.write(buffer);
                bout.flush();
            } catch (FileNotFoundException e) {
                LOGGER.error("copy cert failed:", e);
            } catch (IOException e) {
                LOGGER.error("copy cert failed:", e);
            }
        }
    }

    public static void main(String[] args) {
        Thread startThread = new Thread(() -> {
            start();
        }, "main-start");
        startThread.start();
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
