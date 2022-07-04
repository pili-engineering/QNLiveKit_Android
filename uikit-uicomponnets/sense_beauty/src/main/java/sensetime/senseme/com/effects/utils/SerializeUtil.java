package sensetime.senseme.com.effects.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 2019/4/4 16:10
 */
public class SerializeUtil {

    // 文件路径
    public static String ROOT_FILE_DIR = Environment.getExternalStorageDirectory() + File.separator + "User" + File.separator;
    // 文件名字
    public static String USER_STATE_FILE_NAME_DIR = "STPointArray";

    public static boolean createOrExistsDir(final File file) {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 删除目录
     *
     * @param dir 目录
     * @return {@code true}: 删除成功<br>{@code false}: 删除失败
     */
    public static boolean deleteDir(final File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 序列化，对象存入SD卡
     *
     * @param obj          存储对象
     * @param destFileDir  SD卡目标路径
     * @param destFileName SD卡文件名
     */
    public static void writeObjectToSDCard(Object obj, String destFileDir, String destFileName) {
        createOrExistsDir(new File(destFileDir));
        deleteDir(new File(destFileDir + destFileName));
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(destFileDir, destFileName));
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                    objectOutputStream = null;
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                    fileOutputStream = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 反序列化，从SD卡取出对象
     *
     * @param destFileDir  SD卡目标路径
     * @param destFileName SD卡文件名
     */
    public static Object readObjectFromSDCard(String destFileDir, String destFileName) {
        FileInputStream fileInputStream = null;
        Object object = null;
        ObjectInputStream objectInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(destFileDir, destFileName));
            objectInputStream = new ObjectInputStream(fileInputStream);
            object = objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                    objectInputStream = null;
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                    fileInputStream = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    /**
     * 反序列化，从SD卡取出对象
     *
     * @param destFileDir  SD卡目标路径
     */
    public static Object readObjectFromAssets(Context context, String destFileDir) {
        AssetManager assetManager = context.getResources().getAssets();


        InputStream fileInputStream = null;
        Object object = null;
        ObjectInputStream objectInputStream = null;
        try {
            //fileInputStream = new FileInputStream(new File(destFileDir, destFileName));
            fileInputStream = assetManager.open(destFileDir);

            objectInputStream = new ObjectInputStream(fileInputStream);
            object = objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                    objectInputStream = null;
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                    fileInputStream = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

}
