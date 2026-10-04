package secure_app.backend.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import secure_app.backend.exception.EncryptionException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionService {

    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;

    private final SecretKey key;
    private final SecureRandom secureRandom;

    public EncryptionService(
            @Value("${encryption.key}") String encodedKey
    ) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(encodedKey);

            if (decodedKey.length != 32) {
                throw new IllegalArgumentException(
                        "Encryption key must be exactly 32 bytes"
                );
            }

            this.key = new SecretKeySpec(decodedKey, "AES");
            this.secureRandom = new SecureRandom();

        } catch (IllegalArgumentException e) {
            throw new EncryptionException(
                    "Failed to initialize encryption service",
                    e
            );
        }
    }

    public String encrypt(String text) {
        try {
            // Generate random IV for each encryption
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            GCMParameterSpec spec =
                    new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            cipher.init(Cipher.ENCRYPT_MODE, key, spec);

            byte[] encrypted = cipher.doFinal(
                    text.getBytes(StandardCharsets.UTF_8)
            );

            // Prepend IV to encrypted data: [IV][encrypted_data]
            byte[] result = new byte[IV_LENGTH + encrypted.length];

            System.arraycopy(
                    iv,
                    0,
                    result,
                    0,
                    IV_LENGTH
            );

            System.arraycopy(
                    encrypted,
                    0,
                    result,
                    IV_LENGTH,
                    encrypted.length
            );

            return Base64.getEncoder()
                    .encodeToString(result);

        } catch (GeneralSecurityException e) {
            throw new EncryptionException(
                    "Failed to encrypt message",
                    e
            );
        }
    }

    public String decrypt(String encryptedText) {
        try {
            byte[] data = Base64.getDecoder()
                    .decode(encryptedText);

            if (data.length <= IV_LENGTH) {
                throw new IllegalArgumentException(
                        "Invalid encrypted message"
                );
            }

            // Extract IV from the beginning of data
            byte[] iv = new byte[IV_LENGTH];

            byte[] encrypted = new byte[data.length - IV_LENGTH];

            System.arraycopy(
                    data,
                    0,
                    iv,
                    0,
                    IV_LENGTH
            );

            System.arraycopy(
                    data,
                    IV_LENGTH,
                    encrypted,
                    0,
                    encrypted.length
            );

            Cipher cipher = Cipher.getInstance(
                    "AES/GCM/NoPadding"
            );

            GCMParameterSpec spec =
                    new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            byte[] decrypted = cipher.doFinal(encrypted);

            return new String(
                    decrypted,
                    StandardCharsets.UTF_8
            );

        } catch (GeneralSecurityException | IllegalArgumentException e) {
            throw new EncryptionException(
                    "Failed to decrypt message",
                    e
            );
        }
    }
}