package io.mobimenu.common.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response payload for api related errors
 *
 * @param code      api specific error code
 * @param message   human-readable description of the api error
 */
public record ApiErrorResponse(
        @JsonProperty("code") int code,
        @JsonProperty("message") String message) {
}
