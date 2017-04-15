package moe.haruue.walkee.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Store and get something without RAM cache, for the common data of IPC
 * <p>this class managed a set of little files and use them to read and write fast</p>
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class KVUtils {

    public static final String TAG = KVUtils.class.getSimpleName();

    private static final String DEFAULT_KV_DIR_NAME = "keys_and_values";

    private static final Object LOCK = new Object();

    public static void set(Context context, String key, Object object) {
        set(context, key, object, DEFAULT_KV_DIR_NAME);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void set(Context context, String key, Object object, String kvDirName) {
        synchronized (LOCK) {
            File file = getFile(context, kvDirName, key);
            file.delete();
            if (object != null) {
                writeObjectToFile(file, object);
            }
        }
    }

    public static Object get(Context context, String key) {
        return get(context, key, 0);
    }

    public static Object get(Context context, String key, Object defaultObject) {
        return get(context, key, defaultObject, DEFAULT_KV_DIR_NAME);
    }

    public static Object get(Context context, String key, Object defaultObject, String dirName) {
        synchronized (LOCK) {
            File file = getFile(context, dirName, key);
            return getObjectFormFile(file, defaultObject);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File getFile(Context context, String directory, String name) {
        String dirFullPath = context.getFilesDir() + File.separator + directory;
        File dir = new File(dirFullPath);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.delete();
            dir.mkdirs();
        }
        String fileFullPath = dirFullPath + File.separator + name;
        File file = new File(fileFullPath);
        if (!file.isFile()) {
            file.delete();
        }
        return file;
    }

    private static Object getObjectFormFile(File file, Object defaultObject) {
        Object result;
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
            result = is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Log.w(TAG, "getDirectFormFile: can't read", e);
            result = defaultObject;
        }
        return result;
    }

    private static void writeObjectToFile(File file, Object o) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
            os.writeObject(o);
        } catch (IOException e) {
            Log.w(TAG, "writeObjectToFile: can't write", e);
        }
    }


}
