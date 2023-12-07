package edu.esp.utilities;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Hasher {

    public static int hash(String input) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Apply SHA-256 hashing to the input
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            // Get hashCode of the byte array representing the hash
            int shaInt = Arrays.hashCode(hash);
            // In case shaInt is -1, changed to prevent conflicts with the default return value in case of exception
            if (shaInt == -1)
                shaInt = Integer.MAX_VALUE;

            return shaInt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Return default value in case of exception occurs
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(Hasher.hash("1234"));
    }
}
