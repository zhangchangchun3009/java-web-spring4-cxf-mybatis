package org.springframework.boot.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;

import org.springframework.boot.loader.archive.Archive;

/**
 * Base class for executable archive {@link Launcher}s.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 */
public abstract class ExecutableArchiveLauncher extends Launcher {

    private final Archive archive;

    public ExecutableArchiveLauncher() {
        try {
            this.archive = createArchive();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    protected ExecutableArchiveLauncher(Archive archive) {
        this.archive = archive;
    }

    protected final Archive getArchive() {
        return this.archive;
    }

    @Override
    protected String getMainClass() throws Exception {
        return "pers.zcc.scm.web.launch.AppBootstrap";
    }

    @Override
    protected List<Archive> getClassPathArchives() throws Exception {
        List<Archive> archives = new ArrayList<>(this.archive.getNestedArchives(this::isNestedArchive));
        postProcessClassPathArchives(archives);
        return archives;
    }

    /**
     * Determine if the specified {@link JarEntry} is a nested item that should be added
     * to the classpath. The method is called once for each entry.
     * @param entry the jar entry
     * @return {@code true} if the entry is a nested item (jar or folder)
     */
    protected abstract boolean isNestedArchive(Archive.Entry entry);

    /**
     * Called to post-process archive entries before they are used. Implementations can
     * add and remove entries.
     * @param archives the archives
     * @throws Exception if the post processing fails
     */
    protected void postProcessClassPathArchives(List<Archive> archives) throws Exception {
    }

}
