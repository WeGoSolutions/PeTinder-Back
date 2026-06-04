package cruds.Dashboard.V2.core.application.usecase;

import cruds.Dashboard.V2.core.adapter.PetDashboardGateway;
import cruds.Dashboard.V2.core.domain.PetDashboard;

import java.util.List;
import java.util.UUID;

public class ObterRankingPetsUseCase {

    private final PetDashboardGateway petDashboardGateway;

    public ObterRankingPetsUseCase(PetDashboardGateway petDashboardGateway) {
        this.petDashboardGateway = petDashboardGateway;
    }

    public List<PetDashboard> obterRanking(UUID ongId) {
        // Uma ONG sem pets e um estado VALIDO (lista vazia), nao um erro 404.
        // Retornar [] deixa o dashboard renderizar vazio normalmente, em vez de
        // o backend responder 404 (que o frontend ja tratava, mas e semantica errada).
        return petDashboardGateway.listarPetsPorOngIdOrderByCurtidas(ongId);
    }
}

