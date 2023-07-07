package io.mobimenu.storage.providers;

import io.mobimenu.storage.StorageFolder;
import io.mobimenu.storage.StorageProvider;
import io.mobimenu.storage.StorageRequest;
import io.mobimenu.storage.StorageResponse;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.concurrent.Executors;

@Slf4j
@RequiredArgsConstructor
public class AmazonS3StorageProvider implements StorageProvider {

    private final S3Client s3Client;
    private final String bucketName;
    private final int executorThreads;

    @Override
    public Uni<StorageResponse> uploadFile(StorageFolder storageFolder, StorageRequest storageRequest) {
        return Uni.createFrom()
                .item(() -> makeStorageCall(storageFolder, storageRequest))
                .map(tuple -> {
                    log.debug("AWS S3 file upload response: {}", tuple.getItem2());
                    var urlRequest = GetUrlRequest.builder().bucket(bucketName).key(tuple.getItem1()).build();
                    return s3Client.utilities().getUrl(urlRequest).toExternalForm();
                })
                .runSubscriptionOn(Executors.newFixedThreadPool(executorThreads))
                .log()
                .map(StorageResponse::from);
    }

    private Tuple2<String, PutObjectResponse> makeStorageCall(StorageFolder storageFolder, StorageRequest storageRequest) {
        var objectKey = "%s/%s".formatted(storageFolder.getPath(), storageRequest.filename());
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .contentEncoding(storageRequest.contentEncoding())
                .contentType(storageRequest.contentType())
                .contentLength(storageRequest.contentLength())
                .build();
        return Tuple2.of(objectKey, s3Client.putObject(request, RequestBody.fromInputStream(storageRequest.inputStream(), storageRequest.contentLength())));
    }

}
