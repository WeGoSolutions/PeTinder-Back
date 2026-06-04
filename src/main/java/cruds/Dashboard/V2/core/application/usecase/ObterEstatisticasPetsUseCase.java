package cruds.Dashboard.V2.core.application.usecase;

import cruds.Dashboard.V2.core.adapter.PetDashboardGateway;

import java.util.UUID;

public class ObterEstatisticasPetsUseCase {

    private final PetDashboardGateway petDashboardGateway;

    public ObterEstatisticasPetsUseCase(PetDashboardGateway petDashboardGateway) {
        this.petDashboardGateway = petDashboardGateway;
    }

    public EstatisticasPets obterEstatisticas(UUID ongId) {
        long adotados = petDashboardGateway.contarPetsAdotadosPorOngId(ongId);
        long naoAdotados = petDashboardGateway.contarPetsNaoAdotadosPorOngId(ongId);

        return new EstatisticasPets((int) adotados, (int) naoAdotados);
    }

    public static class EstatisticasPets {
        private final int adotados;
        private final int naoAdotados;

        public EstatisticasPets(int adotados, int naoAdotados) {
            this.adotados = adotados;
            this.naoAdotados = naoAdotados;
        }

        public int getAdotados() {
            return adotados;
        }

        public int getNaoAdotados() {
            return naoAdotados;
        }
    }
}

