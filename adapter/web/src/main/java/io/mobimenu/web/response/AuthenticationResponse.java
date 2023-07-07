package io.mobimenu.web.response;

/**
 * Authentication response object which contains the auth token generated for the user session
 *
 * @param token    auth token generated for the user session
 */
public record AuthenticationResponse(String token){}
