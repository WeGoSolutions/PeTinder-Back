package cruds.Pets.V2.infrastructure.external;

import cruds.Pets.V2.core.adapter.ArmazenamentoImagemPetGateway;
import cruds.Pets.V2.core.domain.ImagemPet;
import cruds.common.strategy.ImageStorageStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class ArmazenamentoImagemPetAdapter implements ArmazenamentoImagemPetGateway {

    private final ImageStorageStrategy imageStorageStrategy;

    public ArmazenamentoImagemPetAdapter(@Qualifier("localImageStorageStrategy") ImageStorageStrategy imageStorageStrategy) {
        this.imageStorageStrategy = imageStorageStrategy;
    }

    @Override
    public String salvarImagem(ImagemPet imagem) {
        try {
            String nomeArquivo = imagem.getNomeArquivo();
            if (nomeArquivo == null) {
                nomeArquivo = "pet_" + UUID.randomUUID() + ".jpg";
            }
            
            String caminhoSalvo = imageStorageStrategy.salvarImagem(
                imagem.getDados(), 
                nomeArquivo, 
                "jpg"
            );
            
            return caminhoSalvo;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public void removerImagem(String nomeArquivo) {
        try {
            String caminho = imageStorageStrategy.gerarCaminho(nomeArquivo);
            Files.deleteIfExists(Paths.get(caminho));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao remover imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public ImagemPet buscarImagem(String nomeArquivo) {
        try {
            String caminho = imageStorageStrategy.gerarCaminho(nomeArquivo);
            byte[] dados = Files.readAllBytes(Paths.get(caminho));
            return new ImagemPet(caminho, nomeArquivo, dados);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public String gerarUrlAcesso(String nomeArquivo) {
        // Para armazenamento local, retorna um caminho relativo que pode ser usado pela API
        return "/pets/imagens/" + nomeArquivo;
    }

    @Override
    public byte[] buscarDadosImagem(String caminho) {
        try {
            return Files.readAllBytes(Paths.get(caminho));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler dados da imagem: " + e.getMessage(), e);
        }
    }
}