package cruds.Pets.V2.infrastructure.external;

import cruds.Pets.V2.core.adapter.ArmazenamentoImagemPetGateway;
import cruds.Pets.V2.core.domain.ImagemPet;
import cruds.Users.V2.core.domain.ImagemUsuario;
import cruds.common.cryptography.AesCriptografiaAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "app.storage.type", havingValue = "s3")
public class S3StoragePetAdapter implements ArmazenamentoImagemPetGateway {

    private final S3Client s3Client;
    private final String bucketName;
    private final AesCriptografiaAdapter criptografiaAdapter;

    public S3StoragePetAdapter(S3Client s3Client,
                               @Value("${aws.s3.bucket}") String bucketName,
                               AesCriptografiaAdapter criptografiaAdapter) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.criptografiaAdapter = criptografiaAdapter;
    }

    @Override
    public String salvarImagem(ImagemPet imagemPet) {
        UUID id = imagemPet.getId() != null ? imagemPet.getId() : UUID.randomUUID();
        String key = imagemPet.getNomeArquivo() + "-" + id;

        byte[] imagemCriptografada = criptografiaAdapter.criptografarImagem(imagemPet.getDados());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromBytes(imagemCriptografada)
        );

        String region = "us-east-1";

        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName,
                region,
                key);
    }

    @Override
    public void removerImagem(String nomeArquivo, UUID idImagem) {
        String key = nomeArquivo + "-" + idImagem;
        s3Client.deleteObject(b -> b.bucket(bucketName).key(key));
    }

    @Override
    public ImagemPet buscarImagem(String nomeArquivo, UUID idImagem) {
        String key = nomeArquivo + "-" + idImagem;

        var response = s3Client.getObjectAsBytes(r ->
                r.bucket(bucketName).key(key));

        byte[] imagemDescriptografada = criptografiaAdapter.descriptografarImagem(response.asByteArray());

        return new ImagemPet(idImagem, nomeArquivo, imagemDescriptografada);
    }
}
