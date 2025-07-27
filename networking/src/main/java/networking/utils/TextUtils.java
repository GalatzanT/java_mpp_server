package networking.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class TextUtils {
    public static String sha256(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(text.getBytes());

            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static String simpleDecode(String text) {
        char[] chars = text.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            chars[i]-=3;
        }
        return new String(chars);
    }
}
