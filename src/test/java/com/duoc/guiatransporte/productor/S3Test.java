package com.duoc.guiatransporte.productor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@SpringBootTest
@ActiveProfiles("local")
public class S3Test {

    @Autowired
    private S3Client s3Client;

    @Test
    void testSubidaS3() {
        String bucketName = "guias-despacho-ervr-2026";
        String key = "test/prueba.txt";
        String contenido = "Hola AWS, el sistema esta conectado!";

        s3Client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(key).build(),
                RequestBody.fromString(contenido)
        );
        System.out.println(">>> Subida exitosa a S3!");
    }
}