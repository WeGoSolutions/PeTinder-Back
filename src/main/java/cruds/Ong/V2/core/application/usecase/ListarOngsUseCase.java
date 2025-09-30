package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.domain.Ong;

import java.util.List;

public class ListarOngsUseCase {
    
    private final OngGateway ongGateway;

    public ListarOngsUseCase(OngGateway ongGateway) {
        this.ongGateway = ongGateway;
    }

    public List<Ong> listar() {
        return ongGateway.listarTodos();
    }
}
