package io.mobimenu.storage;

public record StorageResponse(String url) {

    public static StorageResponse from(String url) {
        return new StorageResponse(url);
    }
}
