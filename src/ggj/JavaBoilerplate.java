package ggj;

import java.io.File;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaBoilerplate {
    public static void setupLibrarySupport()
    {
        javaLibraryPath();
        lwjglPath();
    }

    private static void javaLibraryPath() {
        //http://stackoverflow.com/a/24988095/1364757
        try {
            System.setProperty("java.library.path", "lib");
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(JavaBoilerplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void lwjglPath() {
        //helps with locating openAL
        System.setProperty("org.lwjgl.librarypath", new File("lib").getAbsolutePath());
    }
}
