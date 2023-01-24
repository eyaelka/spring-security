package com.example.demospringsecurity.security;
import java.security.SecureRandom;
import java.math.BigInteger;

public final class SecurityConstante {
    public static final String SECRET = getSecret();
    public static final long EXPIRATION_TIME = 864_000_000;//le token expire après 10 jours de sa création
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING ="Authorization";
    private final static String getSecret() {
        SecureRandom rnd = new SecureRandom();
        rnd.setSeed(1234);
        byte[] token = new byte[1000];
        rnd.nextBytes(token);
        return new BigInteger(1, token).toString(16);
    }
}
