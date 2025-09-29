package cruds.Users.V2.infrastructure.external.AWS;

import cruds.Users.V2.core.adapter.ArmazenamentoImagemGateway;
import cruds.Users.V2.core.domain.ImagemUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Component
public class S3StorageAdapter implements ArmazenamentoImagemGateway {

    private final S3Client s3Client;
    private final String bucketName;

    public S3StorageAdapter(S3Client s3Client,
                            @Value("${aws.s3.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public String salvarImagem(ImagemUsuario imagemUsuario) {
        String nomeArquivo = imagemUsuario.getNomeArquivo();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(nomeArquivo)
                .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromBytes(imagemUsuario.getDados())
        );

        String region = "us-east-1";

        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName,
                region,
                nomeArquivo);
    }

    @Override
    public void removerImagem(String nomeArquivo) {
        s3Client.deleteObject(b -> b.bucket(bucketName).key("usuarios/" + nomeArquivo));
    }

    @Override
    public ImagemUsuario buscarImagem(String nomeArquivo) {
        var response = s3Client.getObjectAsBytes(r -> r.bucket(bucketName).key(nomeArquivo));

        return new ImagemUsuario(UUID.randomUUID(), response.asByteArray(), nomeArquivo);

    }

    @Override
    public String gerarUrlAcesso(String nomeArquivo) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName,
                nomeArquivo);
    }
}