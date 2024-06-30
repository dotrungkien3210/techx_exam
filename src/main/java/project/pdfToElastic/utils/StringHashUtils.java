package project.pdfToElastic.utils;

import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringHashUtils {
    private static final Logger log = Logger.getLogger(StringHashUtils.class);

    public StringHashUtils() {

    }

    public String stringToSHA(String inputString) {
        String hashedString;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(inputString.getBytes());

            hashedString = bytesToHex(hashBytes);

//            System.out.println("Hashed string (SHA-256): " + hashedString);

        } catch (NoSuchAlgorithmException e) {
            hashedString = inputString + System.currentTimeMillis();
            log.error("Exception occur: " + e.getMessage());
        }
        return hashedString;
    }


    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
