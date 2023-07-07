package io.mobimenu.common.security;

/**
 * This interface provides security features related to the hashing and comparison
 * of hashes to strings
 */
public interface PasswordSecurity {

    /**
     * Hashes the provided plain-text string
     *
     * @param plainText    the text to hash
     * @return             a hash of the string
     */
    String hash(String plainText);

    /**
     * Compares the hash of the plain-text string to the hash provided
     *
     * @param plainText    the plain text string to whose hash we wish to compare
     * @param hash         the hash to compare to
     * @return             true if there's a match, false otherwise
     */
    boolean matches(String plainText, String hash);

}
