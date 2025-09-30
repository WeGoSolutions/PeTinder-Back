package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.Ong;

import java.util.UUID;

public class BuscarImagemOngUseCase {
    
    private final OngGateway ongGateway;

    public BuscarImagemOngUseCase(OngGateway ongGateway) {
        this.ongGateway = ongGateway;
    }

    public byte[] buscar(UUID ongId) {
        // Buscar ONG
        Ong ong = ongGateway.buscarPorId(ongId)
            .orElseThrow(() -> new OngException.OngNaoEncontradaException(
                "ONG com ID " + ongId + " não encontrada"
            ));

        // Validar se tem imagem
        if (ong.getImagemOng() == null || !ong.getImagemOng().temImagem()) {
            throw new OngException.ImagemNaoEncontradaException(
                "ONG não possui imagem cadastrada"
            );
        }

        return ong.getImagemOng().getDados();
    }
}
