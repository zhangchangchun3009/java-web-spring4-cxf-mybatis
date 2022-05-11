package pers.zcc.scm.web.launch;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.boot.loader.archive.Archive;
import org.springframework.boot.loader.archive.JarFileArchive;
import org.springframework.boot.loader.jar.JarFile;

public class Launcher0 {
    static final String[] BOOT_INF_CLASSES = new String[] { "org/", "pers/", };

    static final String BOOT_INF_LIB = "lib/";

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
                String path = bootURL.toURI().getSchemeSpecificPart();
                File root = new File(path);
                JarFile.registerUrlProtocolHandler();
                JarFileArchive rootArchive = new JarFileArchive(root);
                List<Archive> archives = getClassPathArchives(rootArchive);
                appCL = createClassLoader(appBootCl, archives);
                Thread.currentThread().setContextClassLoader(appCL);
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

    private static ClassLoader createClassLoader(ClassLoader cl, List<Archive> archives) throws Exception {
        List<URL> urls = new ArrayList<>(archives.size());
        for (Archive archive : archives) {
            urls.add(archive.getUrl());
        }
        return new JarClassLoader(cl, urls.toArray(new URL[0]));
    }

    private static List<Archive> getClassPathArchives(Archive archive) throws Exception {
        List<Archive> archives = new ArrayList<>(archive.getNestedArchives((entry) -> {
            return isNestedArchive(entry);
        }));
        return archives;
    }

    private static boolean isNestedArchive(Archive.Entry entry) {
        if (entry.isDirectory()) {
            return Arrays.binarySearch(BOOT_INF_CLASSES, entry.getName(), new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int minLength = Math.min(o1.length(), o2.length());
                    for (int i = 0; i < minLength; i++) {
                        if (o1.charAt(i) - o2.charAt(i) != 0) {
                            return o1.charAt(i) - o2.charAt(i);
                        } else {
                            if (i == minLength - 1) {
                                return o1.length() - o2.length();
                            } else {
                                continue;
                            }
                        }
                    }
                    return 0;
                }
            }) > 0;
        }
        return entry.getName().startsWith(BOOT_INF_LIB);
    }

}
