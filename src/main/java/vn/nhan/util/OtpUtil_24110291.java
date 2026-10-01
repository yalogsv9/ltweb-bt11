package vn.nhan.util;

import java.security.SecureRandom;

public class OtpUtil_24110291 {
    private static final SecureRandom RANDOM = new SecureRandom();

    /** Sinh ma OTP 6 chu so. */
    public static String generate() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }
}
