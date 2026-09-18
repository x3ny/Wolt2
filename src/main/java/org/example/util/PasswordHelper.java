package org.example.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHelper {

    private final BCryptPasswordEncoder encoder;

    public PasswordHelper() {
        this.encoder = new BCryptPasswordEncoder();
    }

    public String hashPassword(String password) {
        return encoder.encode(password);
    }

    public boolean matches (String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }

}
