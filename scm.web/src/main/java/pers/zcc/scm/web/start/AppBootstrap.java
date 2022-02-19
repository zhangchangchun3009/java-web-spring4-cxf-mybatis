package pers.zcc.scm.web.start;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

/**
 * 嵌入式tomcat启动类
 * @author zhangchangchun
 * @Date 2022年2月19日
 */
public class AppBootstrap {

    public static void main(String[] args) {
        Tomcat tomcat = new Tomcat();
        int port = 8080;
        tomcat.setPort(port);
        System.out.println("port:" + port);
        String baseDir = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println("baseDir:" + baseDir);
        tomcat.setBaseDir(baseDir);
        String contextPath = "/scm.web";
        try {
            tomcat.addWebapp(contextPath, baseDir);
        } catch (ServletException e) {
            e.printStackTrace();
        }
        tomcat.enableNaming();
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
        tomcat.getServer().await();
    }

}
