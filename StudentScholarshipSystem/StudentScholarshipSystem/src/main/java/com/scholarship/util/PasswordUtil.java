
package com.scholarship.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    public static String hashPassword(String password)
            throws Exception {

        byte[] salt = new byte[16];

        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                210000,
                256
        );

        SecretKeyFactory factory =
                SecretKeyFactory.getInstance(
                        "PBKDF2WithHmacSHA256"
                );

        byte[] hash = factory.generateSecret(spec).getEncoded();

        spec.clearPassword();

        return Base64.getEncoder().encodeToString(salt)
                + ":"
                + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(
            String password, String storedHash)
            throws Exception {

        String[] parts = storedHash.split(":");

        byte[] salt = Base64.getDecoder().decode(parts[0]);

        byte[] originalHash =
                Base64.getDecoder().decode(parts[1]);

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                210000,
                256
        );

        SecretKeyFactory factory =
                SecretKeyFactory.getInstance(
                        "PBKDF2WithHmacSHA256"
                );

        byte[] newHash = factory.generateSecret(spec).getEncoded();

        spec.clearPassword();

        return java.security.MessageDigest.isEqual(
                originalHash, newHash
        );
    }
}
