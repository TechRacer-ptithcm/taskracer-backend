package ptithcm.itmc.taskracer.util.jwt;

import java.math.BigInteger;
import java.security.SecureRandom;

public class CommonUtil {
    public static String generateOtpCode() {
        SecureRandom secureRandom = new SecureRandom();
        BigInteger minValue = new BigInteger("100000");
        BigInteger maxValue = new BigInteger("999999");
        BigInteger range = maxValue.subtract(minValue).add(BigInteger.ONE);
        BigInteger otpCode = new BigInteger(range.bitLength(), secureRandom).mod(range).add(minValue);
        return otpCode.toString();
    }
}
