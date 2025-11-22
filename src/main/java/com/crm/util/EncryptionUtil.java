package com.crm.util;

import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtil {

    public static String generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 256-bit key
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String generatePin() {
        SecureRandom random = new SecureRandom();
        int pin = 100000 + random.nextInt(900000); // 6 digit PIN
        return String.valueOf(pin);
    }
}
