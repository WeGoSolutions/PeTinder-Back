package cruds.Dashboard.V2.core.application.usecase;

import cruds.Dashboard.V2.core.adapter.PetDashboardGateway;
import cruds.Dashboard.V2.core.domain.PetDashboard;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ListarPendenciasPetsUseCase {

    private final PetDashboardGateway petDashboardGateway;

    public ListarPendenciasPetsUseCase(PetDashboardGateway petDashboardGateway) {
        this.petDashboardGateway = petDashboardGateway;
    }

    public List<PetDashboard> listarPendencias(UUID ongId) {
        // ONG sem pets = lista vazia (nao 404). So retornamos os pets que de fato
        // tem pendencias; nenhum pet -> [] e o dashboard mostra vazio.
        return petDashboardGateway.listarPetsPorOngId(ongId).stream()
                .filter(PetDashboard::temPendencias)
                .collect(Collectors.toList());
    }
}

