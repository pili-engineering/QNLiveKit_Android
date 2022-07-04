package sensetime.senseme.com.effects.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import sensetime.senseme.com.effects.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ImageUtils {
    /**
     * 同步
     *
     * @param url 图片地址
     * @return
     * @throws Exception
     */
    public static Bitmap getImageSync(String url, Context context) throws Exception {
        if (TextUtils.isEmpty(url)) return null;
        String imageName = md5(url);
        String subPath = "/images/";
        File imageFile = new File(context.getCacheDir().getAbsolutePath() + subPath + imageName + ".png");
        if (imageFile.exists()) {
            LogUtils.d("ImageUtiles", "cache read image :" + imageFile.toString());
            return BitmapFactory.decodeFile(imageFile.toString());
        }
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inStream);
            inStream.close();
            new File(context.getCacheDir().getAbsolutePath() + subPath).mkdirs();
            saveImageFile(imageFile, bitmap);
            return bitmap;
        }
        return null;
    }

    //保存图片到本地
    private static void saveImageFile(File file, Bitmap bitmap) {
        if (file == null || bitmap == null) {
           // LogUtils.e("", "image file or bitmap can't be null");
            return;
        }
      //  LogUtils.d("ImageUtiles", "image cached path is " + file.toString());
        file.deleteOnExit();
        try {
            file.createNewFile();
        } catch (IOException e) {

        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
