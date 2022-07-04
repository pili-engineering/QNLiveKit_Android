package sensetime.senseme.com.effects.utils;


import android.util.Base64;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    private static final String TAG = "AESUtil";

    private static String charset = "utf-8";
    private static int offset = 16;
    private static String transformation = "AES/CBC/PKCS5Padding";
    private static String algorithm = "AES";

    public static String decrypt(String content, String key) {
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), algorithm);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes(), 0, offset);
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, skey, iv);
            byte[] result = cipher.doFinal(decodeFromString(content));
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decodeFromString(String content) throws UnsupportedEncodingException {
        byte[] bytes = content.getBytes("UTF-8");
        byte[] decode = Base64.decode(bytes, Base64.NO_WRAP);
        return decode;
    }
}