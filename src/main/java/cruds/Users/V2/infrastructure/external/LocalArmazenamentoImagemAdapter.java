package cruds.Users.V2.infrastructure.external;

import cruds.Users.V2.core.adapter.ArmazenamentoImagemGateway;
import cruds.Users.V2.core.domain.ImagemUsuario;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@ConditionalOnProperty(name = "app.storage.type", havingValue = "local", matchIfMissing = true)
public class LocalArmazenamentoImagemAdapter implements ArmazenamentoImagemGateway {

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/Desktop/S3 local/imagens/";

    @Override
    public String salvarImagem(ImagemUsuario imagem) {
        try {
            String nomeArquivo = imagem.getNomeArquivo();
            String caminhoCompleto = UPLOAD_DIR + nomeArquivo;
            
            // Criar diretório se não existir
            Path dirPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            
            // Salvar arquivo
            Path filePath = Paths.get(caminhoCompleto);
            Files.write(filePath, imagem.getDados());
            
            return caminhoCompleto;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public void removerImagem(String nomeArquivo) {
        try {
            String caminhoCompleto = UPLOAD_DIR + nomeArquivo;
            Path filePath = Paths.get(caminhoCompleto);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao remover imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public ImagemUsuario buscarImagem(String nomeArquivo) {
        try {
            String caminhoCompleto = UPLOAD_DIR + nomeArquivo;
            Path filePath = Paths.get(caminhoCompleto);
            if (Files.exists(filePath)) {
                byte[] dados = Files.readAllBytes(filePath);
                return new ImagemUsuario(dados, nomeArquivo);
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public String gerarUrlAcesso(String nomeArquivo) {
        return UPLOAD_DIR + nomeArquivo;
    }
}