package io.mobimenu.web.context;

import io.mobimenu.storage.StorageProvider;
import io.mobimenu.storage.providers.AmazonS3StorageProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@ApplicationScoped
public class StorageContext {

    @Singleton
    @Produces
    public StorageProvider storageProvider(@ConfigProperty(name = "amazon.s3.bucket.name") String bucketName,
                                           @ConfigProperty(name = "amazon.s3.client.executor.threads") int numOfThreads) {
        var s3Client = S3Client.builder().region(Region.US_EAST_1).build();
        return new AmazonS3StorageProvider(s3Client, bucketName, numOfThreads);
    }

}
