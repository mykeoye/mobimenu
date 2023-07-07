package io.mobimenu.common.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Default password security provider, this implementation uses the bcrypt password hashing
 * function for hashing passwords.
 *
 * <p>Read more here: https://en.wikipedia.org/wiki/Bcrypt</p>
 */
public class DefaultPasswordSecurityProvider implements PasswordSecurity {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Override
    public String hash(String plainText) {
        return ENCODER.encode(plainText);
    }

    @Override
    public boolean matches(String plainText, String hash) {
        return ENCODER.matches(plainText, hash);
    }

}
