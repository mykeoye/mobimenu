package io.mobimenu.storage;

import io.smallrye.mutiny.Uni;

/**
 * This interface provides an abstraction for cloud storage. Implementors of this interface
 */
public interface StorageProvider {

    /**
     * Uploads a file to the backing store
     *
     * @param folderPath        folder (logical) where the file should be stored
     * @param uploadRequest     the upload request
     * @return                  storage response object containing details of the upload
     */
    Uni<StorageResponse> uploadFile(StorageFolder folderPath, StorageRequest uploadRequest);

}
