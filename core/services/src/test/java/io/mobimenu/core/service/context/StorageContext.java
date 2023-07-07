package io.mobimenu.core.service.context;

import io.mobimenu.storage.StorageFolder;
import io.mobimenu.storage.StorageProvider;
import io.mobimenu.storage.StorageRequest;
import io.mobimenu.storage.StorageResponse;
import io.smallrye.mutiny.Uni;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class StorageContext {

    @Singleton
    @Produces
    public StorageProvider storageProvider() {
        return new StorageProvider() {
            @Override
            public Uni<StorageResponse> uploadFile(StorageFolder folderPath, StorageRequest uploadRequest) {
                return Uni.createFrom().item(() -> new StorageResponse("https://bucket.s3.com/qr-9888339939"));
            }
        };
    }

}
