package cruds.Users.V2.infrastructure.external;

import cruds.Users.V2.core.adapter.ArmazenamentoImagemGateway;
import cruds.Users.V2.core.domain.ImagemUsuario;
import cruds.common.strategy.ImageStorageStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ImageStorageAdapter implements ArmazenamentoImagemGateway {

    private final ImageStorageStrategy imageStorageStrategy;

    public ImageStorageAdapter(@Qualifier("localImageStorageStrategy") ImageStorageStrategy imageStorageStrategy) {
        this.imageStorageStrategy = imageStorageStrategy;
    }

    @Override
    public String salvarImagem(ImagemUsuario imagem) {
        try {
            return imageStorageStrategy.upload(imagem.getDados(), imagem.getNomeArquivo());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public void removerImagem(String nomeArquivo) {
        try {
            imageStorageStrategy.delete(nomeArquivo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public ImagemUsuario buscarImagem(String nomeArquivo) {
        try {
            byte[] dados = imageStorageStrategy.download(nomeArquivo);
            return new ImagemUsuario(dados, nomeArquivo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar imagem: " + e.getMessage(), e);
        }
    }

    @Override
    public String gerarUrlAcesso(String nomeArquivo) {
        return imageStorageStrategy.getUrl(nomeArquivo);
    }
}
