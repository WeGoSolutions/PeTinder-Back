package cruds.Pets.V2.infrastructure.external;

import cruds.Pets.V2.core.adapter.ArmazenamentoImagemPetGateway;
import cruds.Pets.V2.core.domain.ImagemPet;
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

    public S3StoragePetAdapter(
            S3Client s3Client,
            @Value("${aws.s3.bucket}") String bucketName,
            AesCriptografiaAdapter criptografiaAdapter
    ) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.criptografiaAdapter = criptografiaAdapter;
    }


    @Override
    public String salvarImagem(ImagemPet imagemPet, UUID petId) {
        UUID id = imagemPet.getId() != null ? imagemPet.getId() : UUID.randomUUID();
        imagemPet.setId(id);

        String key = String.format("%s-%s-%s", petId, id, imagemPet.getNomeArquivo());

        byte[] dadosCriptografados = criptografiaAdapter.criptografarImagem(imagemPet.getDados());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(dadosCriptografados));

        imagemPet.setKeyS3(key);

        return key;
    }

    @Override
    public void removerImagem(String nomeArquivo, UUID idImagem) {
        // Key format must match the one used in salvarImagem: <petId>-<id>-<nomeArquivo>
        // Since removerImagem recebe apenas idImagem e nomeArquivo, tentamos corresponder ao segmento id.
        // A chave armazenada foi definida como key = String.format("%s-%s-%s", petId, id, nomeArquivo)
        // Tentaremos ambas as variações comuns para ser robusto.
        String key1 = idImagem + "_" + nomeArquivo; // legado
        String key2 = "-" + idImagem + "-" + nomeArquivo; // fallback

        try {
            s3Client.deleteObject(b -> b.bucket(bucketName).key(key1));
        } catch (Exception e) {
            try {
                s3Client.deleteObject(b -> b.bucket(bucketName).key(key2));
            } catch (Exception ignored) {
                // Se ainda falhar, relance a exceção original para sinalizar falha
                throw e;
            }
        }
    }

    @Override
    public ImagemPet buscarImagem(String nomeArquivo, UUID idImagem) {
        String key1 = idImagem + "_" + nomeArquivo;
        String key2 = "-" + idImagem + "-" + nomeArquivo;

        var response = tryGetObject(key1);
        if (response == null) {
            response = tryGetObject(key2);
            if (response == null) {
                throw new RuntimeException("Imagem não encontrada no S3 para as chaves: " + key1 + " ou " + key2);
            }
        }

        byte[] imagemDescriptografada = criptografiaAdapter.descriptografarImagem(response.asByteArray());

        return new ImagemPet(idImagem, nomeArquivo, imagemDescriptografada, key1);
    }

    // Helper para tentativa de download sem lançar exceção imediata
    private software.amazon.awssdk.core.ResponseBytes<software.amazon.awssdk.services.s3.model.GetObjectResponse> tryGetObject(String key) {
        try {
            return s3Client.getObjectAsBytes(r -> r.bucket(bucketName).key(key));
        } catch (Exception e) {
            return null;
        }
    }

}
