package cruds.Ong.V2.infrastructure.external;

import cruds.Ong.V2.core.adapter.ArmazenamentoImagemGateway;
import cruds.common.strategy.ImageStorageStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ArmazenamentoImagemOngAdapter implements ArmazenamentoImagemGateway {

    private final ImageStorageStrategy imageStorageStrategy;

    public ArmazenamentoImagemOngAdapter(@Qualifier("localImageStorageStrategy") ImageStorageStrategy imageStorageStrategy) {
        this.imageStorageStrategy = imageStorageStrategy;
    }

    @Override
    public String salvarImagem(byte[] dados, String caminhoCompleto) {
        try {
            // Criar diretório se não existir
            Files.createDirectories(Paths.get(caminhoCompleto).getParent());
            
            // Salvar arquivo
            Files.write(Paths.get(caminhoCompleto), dados);
            
            return caminhoCompleto;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] recuperarImagem(String caminhoCompleto) {
        try {
            return Files.readAllBytes(Paths.get(caminhoCompleto));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao recuperar imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public void removerImagem(String caminhoCompleto) {
        try {
            Files.deleteIfExists(Paths.get(caminhoCompleto));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao remover imagem: " + e.getMessage(), e);
        }
    }
}
