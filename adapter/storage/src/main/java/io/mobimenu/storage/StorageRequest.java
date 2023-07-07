package io.mobimenu.storage;

import software.amazon.awssdk.utils.StringUtils;
import java.io.InputStream;

public record StorageRequest(String filename, InputStream inputStream, long contentLength, String contentType, String contentEncoding) {

    public StorageRequest {
        if (StringUtils.isBlank(filename)) {
            throw new IllegalArgumentException("Filename must be provided and cannot be blank");
        }
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        if (contentLength < 0) {
            throw new IllegalArgumentException("Content length must be >= 0");
        }
    }

}
