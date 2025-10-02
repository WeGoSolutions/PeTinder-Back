package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.ArmazenamentoImagemOngGateway;
import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.command.UploadImagemOngCommand;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.ImagemOng;
import cruds.Ong.V2.core.domain.Ong;

public class UploadImagemOngUseCase {

    private final OngGateway ongGateway;
    private final ArmazenamentoImagemOngGateway armazenamentoGateway;

    public UploadImagemOngUseCase(OngGateway ongGateway,
                                  ArmazenamentoImagemOngGateway armazenamentoGateway) {
        this.ongGateway = ongGateway;
        this.armazenamentoGateway = armazenamentoGateway;
    }

    public Ong executar(UploadImagemOngCommand command) {
        Ong ong = ongGateway.buscarPorId(command.getOngId())
            .orElseThrow(() -> new OngException.OngNaoEncontradaException(
                "ONG não encontrada com id: " + command.getOngId()
            ));

        try {
            String caminhoArquivo = armazenamentoGateway.salvarImagem(
                command.getImagemBytes(),
                command.getOngId()
            );

            ImagemOng imagem = new ImagemOng(command.getImagemBytes(), caminhoArquivo);

            if (ong.getImagemOng() != null) {
                imagem.setId(ong.getImagemOng().getId());
            }

            ong.definirImagem(imagem);

            return ongGateway.atualizar(ong);
        } catch (Exception e) {
            throw new OngException.ErroArmazenamentoException(
                "Erro ao salvar imagem da ONG", e
            );
        }
    }
}

