package io.mobimenu.common.util;

public interface AuthenticationTokenProvider<T> {

    String generateToken(T principal);

}
