package pers.zcc.scm.web.launch;

import java.util.Arrays;
import java.util.Comparator;

import org.springframework.boot.loader.archive.Archive;

/**
 * {@link Launcher} for JAR based archives. This launcher assumes that dependency jars are
 * included inside a {@code /BOOT-INF/lib} directory and that application classes are
 * included inside a {@code /BOOT-INF/classes} directory.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
public class JarLauncher extends ExecutableArchiveLauncher {

    static final String[] BOOT_INF_CLASSES = new String[] { "org/", "pers/", };

    static final String BOOT_INF_LIB = "lib/";

    public JarLauncher() {
    }

    protected JarLauncher(Archive archive) {
        super(archive);
    }

    @Override
    protected boolean isNestedArchive(Archive.Entry entry) {
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

    public static void main(String[] args) throws Exception {
        new JarLauncher().launch(args);
    }

}
