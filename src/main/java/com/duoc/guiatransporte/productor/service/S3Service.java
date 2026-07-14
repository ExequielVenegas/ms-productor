package com.duoc.guiatransporte.productor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public byte[] descargarArchivo(String claveS3) {
        return s3Client.getObjectAsBytes(GetObjectRequest.builder().bucket(bucketName).key(claveS3).build()).asByteArray();
    }

    public void eliminarArchivo(String claveS3) {
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(claveS3).build());
    }

    public List<String> listarPorFechaYTransportista(String fecha, String transportista) {
        return s3Client.listObjectsV2(ListObjectsV2Request.builder().bucket(bucketName).prefix(fecha + "/" + transportista + "/").build())
                .contents().stream().map(S3Object::key).collect(Collectors.toList());
    }
}