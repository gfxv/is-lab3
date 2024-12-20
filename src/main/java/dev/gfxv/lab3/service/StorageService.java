package dev.gfxv.lab3.service;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StorageService {

    @NonFinal
    @Value("${minio.bucket.name}")
    String bucketName;

    MinioClient minioClient;

    @Autowired
    public StorageService(
        MinioClient minioClient
    ) {
        this.minioClient = minioClient;
    }

    @PostConstruct
    public void init() {
        if (bucketName == null || bucketName.isEmpty()) {
            System.out.println("Bucket Name is not initialized!");
        }
    }

    /**
     *
     * @param fileName
     * @param inputStream
     * @param contentType
     * @return File name of stored object
     */
    public String uploadFile(String fileName, InputStream inputStream, String contentType) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            String objectName = UUID.randomUUID() + "-" + fileName;
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(contentType)
                            .build());
            return objectName;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }
    }

    public InputStream downloadFile(String fileName) throws Exception {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (MinioException e) {
            throw new Exception("Error downloading file from MinIO: " + e.getMessage());
        }
    }

    public List<String> listFiles() throws Exception {
        try {
            List<String> fileList = new ArrayList<>();
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            for (Result<Item> result : results) {
                Item item = result.get();
                fileList.add(item.objectName());
            }
            return fileList;
        } catch (Exception e) {
            throw new Exception("Error listing files in the bucket: " + e.getMessage());
        }
    }
}
