package cruds.common.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/s3")
public class S3PresignController {

    private final S3Presigner presigner;
    private final String bucketName;

    public S3PresignController(S3Presigner presigner, @Value("${aws.s3.bucket}") String bucketName) {
        this.presigner = presigner;
        this.bucketName = bucketName;
    }

    @PostMapping("/presign")
    public ResponseEntity<Map<String, String>> presign(@RequestParam String filename, @RequestParam(required = false) String contentType) {
        String key = String.format("%s-%s-%s", UUID.randomUUID(), System.currentTimeMillis(), filename);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType != null ? contentType : "application/octet-stream")
                .build();

        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(p -> p
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(objectRequest)
        );

        Map<String, String> response = new HashMap<>();
        response.put("url", presignedRequest.url().toString());
        response.put("method", "PUT");
        response.put("key", key);

        return ResponseEntity.ok(response);
    }
}
