package classes;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

// Provides password hashing and password verification utilities.
public final class PasswordHasher {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 600_000;
    private static final int SALT_LENGTH = 16;
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    // Prevents this utility class from being instantiated.
    private PasswordHasher() {
    }

    // Creates a salted PBKDF2 hash and formats it for storage.
    public static String hash(String password) {
        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);
        byte[] hash = deriveKey(password, salt, ITERATIONS);

        return "pbkdf2$" + ITERATIONS + "$"
                + encode(salt) + "$" + encode(hash);
    }

    // Hashes a password using the stored salt and compares the results.
    public static boolean matches(String password, String storedPassword) {
        if (password == null || storedPassword == null) {
            return false;
        }

        // Invalid or incomplete stored data cannot match a password.
        try {
            String[] parts = storedPassword.split("\\$", -1);
            if (parts.length != 4) {
                return false;
            }

            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[3]);
            byte[] actualHash = deriveKey(password, salt, iterations);

            return MessageDigest.isEqual(actualHash, expectedHash);
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    // Performs the PBKDF2 key-derivation operation.
    private static byte[] deriveKey(
            String password,
            byte[] salt,
            int iterations
    ) {
        PBEKeySpec keySpec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                iterations,
                KEY_LENGTH
        );

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            return factory.generateSecret(keySpec).getEncoded();
        } catch (java.security.GeneralSecurityException exception) {
            throw new IllegalStateException(
                    "Password hashing is unavailable",
                    exception
            );
        } finally {
            // Removes the password from the key specification when finished.
            keySpec.clearPassword();
        }
    }

    // Converts binary data into Base64 text so it can be stored in a file.
    private static String encode(byte[] value) {
        return Base64.getEncoder().encodeToString(value);
    }
}
