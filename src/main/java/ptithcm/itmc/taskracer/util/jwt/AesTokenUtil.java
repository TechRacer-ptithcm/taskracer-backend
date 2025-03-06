package ptithcm.itmc.taskracer.util.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Component
@Slf4j
public class AesTokenUtil {
    @Value("${task-racer.private-key}")
    private String SECRET_KEY;
    private final String ALGORITHM = "AES";

    public String encrypt(String username, String email, long expiryTime) throws Exception {
        String data = username + "&" + email + "&" + expiryTime;
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String[] decrypt(String encryptedToken) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedToken));
        String decryptedData = new String(decryptedBytes, StandardCharsets.UTF_8);
        // Phân tích dữ liệu (username&email&timestamp)
        String[] parts = decryptedData.split("&");
        log.info("{}", (Object) parts);
        if (parts.length != 3) {
            throw new RuntimeException("Invalid token format");
        }

        long expiryTime = Long.parseLong(parts[2]);

        // Kiểm tra token có hết hạn không
        if (Instant.now().toEpochMilli() > expiryTime) {
            throw new RuntimeException("Token has expired");
        }

        return parts;
    }
}
