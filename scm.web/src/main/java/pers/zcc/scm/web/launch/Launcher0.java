package pers.zcc.scm.web.launch;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.springframework.boot.loader.archive.Archive;

public class Launcher0 {
    private static final String LIB_PATH = "lib/";

    public static void main(String[] args) {
        ClassLoader appCL = null;
        try {
            ClassLoader appBootCl = Launcher0.class.getClassLoader();
            URL bootURL = Launcher0.class.getProtectionDomain().getCodeSource().getLocation();
            String bootPath = bootURL.getPath();
            System.out.println("bootURL：" + bootPath);
            String rootPath = new File(bootPath).getCanonicalPath();
            System.out.println("rootPath:" + rootPath);
            if (bootPath.endsWith(".jar")) { //its hard to analysis jars in jar,give it up ,spring boot loader may help
                Enumeration<URL> urls = appBootCl.getResources(LIB_PATH);
                String path = bootURL.toURI().getSchemeSpecificPart();
                File root = new File(path);
                List<URL> list = new ArrayList<URL>();
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    System.out.println(url.getPath());
                    list.add(url);
                }
                appCL = createClassLoader(appBootCl, null);
                Thread.currentThread().setContextClassLoader(appCL);
                appCL.loadClass("com.fasterxml.jackson.databind.util.BeanUtil");
                Class<?> appBootStrap = appCL.loadClass("pers.zcc.scm.web.launch.AppBootstrap");
                Method main = appBootStrap.getDeclaredMethod("main", new String[] {}.getClass());
                main.invoke(null, new Object[] { new String[] {} });
            } else {
                AppBootstrap.main(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    static ClassLoader createClassLoader(ClassLoader cl, List<Archive> archives) throws Exception {
        List<URL> urls = new ArrayList<>(archives.size());
        for (Archive archive : archives) {
            urls.add(archive.getUrl());
        }
        return new JarClassLoader(cl, urls.toArray(new URL[0]));
    }

}
