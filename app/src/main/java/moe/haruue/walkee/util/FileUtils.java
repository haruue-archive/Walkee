package moe.haruue.walkee.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class FileUtils {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void move(File src, File dst) {
        FileInputStream r = null;
        FileOutputStream w = null;
        try {
            if (dst.exists()) {
                dst.delete();
            }
            r = new FileInputStream(src);
            w = new FileOutputStream(dst);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = r.read(buffer)) != -1) {
                w.write(buffer, 0, len);
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException("Exception in FileUtils", e);
        } finally {
            try {
                if (r != null) {
                    r.close();
                }
                if (w != null) {
                    w.close();
                }
            } catch (Exception ignored) {

            }

        }
    }

}
