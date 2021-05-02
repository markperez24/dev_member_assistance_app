package com.member.assistance.common.utility;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class IDGeneratorUtils {
    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();

    protected IDGeneratorUtils() {
    }

    /**
     * https://stackoverflow.com/questions/30953662/how-to-generate-otp-number-with-6-digits/32468376
     *
     * @param size
     * @return
     */
    public static String randomStringDigits(int size) {

        StringBuilder generatedToken = new StringBuilder();
        try {
            SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
            // Generate 20 integers 0..20
            for (int i = 0; i < size; i++) {
                generatedToken.append(number.nextInt(9));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedToken.toString();
    }

    public static String randomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
