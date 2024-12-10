package dev.gfxv.lab2.security;


import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;

public class SHA512Encoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashedPassword = md.digest(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return MessageDigest.isEqual(
                Utf8.encode(encode(rawPassword)),
                Utf8.encode(encodedPassword)
        );
    }
}
