package pers.zcc.scm.web.launch;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.jar.JarFile;

public class JarClassLoader extends URLClassLoader {

    public JarClassLoader(ClassLoader cl, URL... urls) {
        super(urls, cl);
    }

    @Override
    public Class<?> loadClass(final String name, final boolean resolved) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            try {
                System.out.println("JarClassLoader try load:" + name);
                definePackageIfNecessary(name);
                return super.loadClass(name, resolved);
            } catch (ClassNotFoundException e) {
                System.out.println("JarClassLoader try load failed:" + name);
                throw e;
            }
//            return this.getParent().loadClass(name);
        }
    }

    private void definePackageIfNecessary(String className) {
        int lastDot = className.lastIndexOf('.');
        if (lastDot >= 0) {
            String packageName = className.substring(0, lastDot);
            if (getPackage(packageName) == null) {
                try {
                    definePackage(className, packageName);
                } catch (IllegalArgumentException ex) {
                    // Tolerate race condition due to being parallel capable
                    if (getPackage(packageName) == null) {
                        // This should never happen as the IllegalArgumentException
                        // indicates that the package has already been defined and,
                        // therefore, getPackage(name) should not have returned null.
                        throw new AssertionError(
                                "Package " + packageName + " has already been defined " + "but it could not be found");
                    }
                }
            }
        }
    }

    private void definePackage(String className, String packageName) {
        try {
            AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () -> {
                String packageEntryName = packageName.replace('.', '/') + "/";
                String classEntryName = className.replace('.', '/') + ".class";
                for (URL url : getURLs()) {
                    try {
                        URLConnection connection = url.openConnection();
                        if (connection instanceof JarURLConnection) {
                            JarFile jarFile = ((JarURLConnection) connection).getJarFile();
                            if (jarFile.getEntry(classEntryName) != null && jarFile.getEntry(packageEntryName) != null
                                    && jarFile.getManifest() != null) {
                                definePackage(packageName, jarFile.getManifest(), url);
                                return null;
                            }
                        }
                    } catch (IOException ex) {
                        // Ignore
                    }
                }
                return null;
            }, AccessController.getContext());
        } catch (java.security.PrivilegedActionException ex) {
            // Ignore
        }
    }

}
