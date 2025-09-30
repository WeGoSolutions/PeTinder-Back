package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.Ong;

import java.util.UUID;

public class BuscarOngPorIdUseCase {
    
    private final OngGateway ongGateway;

    public BuscarOngPorIdUseCase(OngGateway ongGateway) {
        this.ongGateway = ongGateway;
    }

    public Ong buscar(UUID id) {
        return ongGateway.buscarPorId(id)
            .orElseThrow(() -> new OngException.OngNaoEncontradaException(
                "ONG com ID " + id + " não encontrada"
            ));
    }
}
