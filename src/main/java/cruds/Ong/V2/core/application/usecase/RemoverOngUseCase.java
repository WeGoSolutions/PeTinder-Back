package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.exception.OngException;

import java.util.UUID;

public class RemoverOngUseCase {
    
    private final OngGateway ongGateway;

    public RemoverOngUseCase(OngGateway ongGateway) {
        this.ongGateway = ongGateway;
    }

    public void remover(UUID id) {
        if (!ongGateway.existePorId(id)) {
            throw new OngException.OngNaoEncontradaException(
                "ONG com ID " + id + " não encontrada"
            );
        }
        
        ongGateway.remover(id);
    }
}
