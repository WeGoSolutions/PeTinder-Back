package cruds.Ong.V2.infrastructure.external;

import cruds.Ong.V2.core.adapter.ArmazenamentoImagemOngGateway;
import cruds.common.strategy.ImageStorageStrategy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ArmazenamentoImagemOngAdapter implements ArmazenamentoImagemOngGateway {

    private final ImageStorageStrategy imageStorageStrategy;
    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/Desktop/S3 local/imagens/";

    public ArmazenamentoImagemOngAdapter(ImageStorageStrategy imageStorageStrategy) {
        this.imageStorageStrategy = imageStorageStrategy;
    }

    @Override
    public String salvarImagem(byte[] dados, UUID ongId) throws Exception {
        String nomeArquivo = "ong_" + ongId + "_perfil.jpg";
        String caminhoCompleto = UPLOAD_DIR + nomeArquivo;
        imageStorageStrategy.salvarImagem(dados, caminhoCompleto);
        return caminhoCompleto;
    }

    @Override
    public byte[] buscarImagem(String caminho) throws Exception {
        // ImageStorageStrategy não possui método de busca
        // As imagens são recuperadas diretamente do banco de dados através da entidade
        throw new UnsupportedOperationException("Buscar imagem não é suportado por ImageStorageStrategy");
    }

    @Override
    public void removerImagem(String caminho) throws Exception {
        // ImageStorageStrategy não possui método de remoção
        // A remoção é feita automaticamente quando a ONG é deletada
        throw new UnsupportedOperationException("Remover imagem não é suportado por ImageStorageStrategy");
    }
}
