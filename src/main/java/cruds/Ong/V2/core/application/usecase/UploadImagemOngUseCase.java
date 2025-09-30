package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.ArmazenamentoImagemGateway;
import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.command.UploadImagemOngCommand;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.ImagemOng;
import cruds.Ong.V2.core.domain.Ong;

public class UploadImagemOngUseCase {
    
    private final OngGateway ongGateway;
    private final ArmazenamentoImagemGateway armazenamentoGateway;
    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/Desktop/S3 local/imagens/";

    public UploadImagemOngUseCase(OngGateway ongGateway, 
                                 ArmazenamentoImagemGateway armazenamentoGateway) {
        this.ongGateway = ongGateway;
        this.armazenamentoGateway = armazenamentoGateway;
    }

    public Ong upload(UploadImagemOngCommand command) {
        // Buscar ONG
        Ong ong = ongGateway.buscarPorId(command.getOngId())
            .orElseThrow(() -> new OngException.OngNaoEncontradaException(
                "ONG com ID " + command.getOngId() + " não encontrada"
            ));

        // Definir nome do arquivo
        String nomeArquivo = command.getNomeArquivo() != null ? 
            command.getNomeArquivo() : "ong_" + command.getOngId() + "_perfil.jpg";
        String caminhoCompleto = UPLOAD_DIR + nomeArquivo;

        // Salvar imagem no armazenamento
        armazenamentoGateway.salvarImagem(command.getDados(), caminhoCompleto);

        // Criar domínio de imagem
        ImagemOng imagemOng = new ImagemOng(command.getDados(), nomeArquivo);
        
        // Atualizar ONG com nova imagem
        ong.atualizarImagemOng(imagemOng);

        // Salvar ONG
        return ongGateway.atualizar(ong);
    }
}
