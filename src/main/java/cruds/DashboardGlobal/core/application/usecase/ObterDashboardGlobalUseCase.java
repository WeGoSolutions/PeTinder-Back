package cruds.DashboardGlobal.core.application.usecase;

import cruds.DashboardGlobal.core.adapter.DadosGoldGateway;
import cruds.DashboardGlobal.core.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObterDashboardGlobalUseCase {

    private final DadosGoldGateway dadosGoldGateway;

    public ObterDashboardGlobalUseCase(DadosGoldGateway dadosGoldGateway) {
        this.dadosGoldGateway = dadosGoldGateway;
    }

    public DashboardGlobalKpis obterKpis() {
        long totalEntradas = dadosGoldGateway.contarTotalEntradas();
        long totalAdotados = dadosGoldGateway.contarAdotados();
        double tempoMedio = dadosGoldGateway.calcularTempoMedioAdocao();
        long totalDevolucoes = dadosGoldGateway.contarDevolucoes();
        long totalAdocoes = dadosGoldGateway.contarAdocoes();

        double taxaAdocao = totalEntradas > 0
                ? ((double) totalAdotados / totalEntradas) * 100
                : 0;

        double taxaRetorno = totalAdocoes > 0
                ? ((double) totalDevolucoes / totalAdocoes) * 100
                : 0;

        return new DashboardGlobalKpis(totalEntradas, taxaAdocao, tempoMedio, taxaRetorno);
    }

    public List<EspecieContagem> obterEntradasPorEspecie() {
        return dadosGoldGateway.listarTopEspecies(5);
    }

    public List<StatusContagem> obterDistribuicaoStatus() {
        return dadosGoldGateway.listarDistribuicaoStatus();
    }

    public List<MotivoRetornoContagem> obterMotivosRetorno() {
        return dadosGoldGateway.listarTopMotivosRetorno(5);
    }

    public List<FaixaEtariaContagem> obterDistribuicaoFaixaEtaria() {
        return dadosGoldGateway.listarDistribuicaoFaixaEtaria();
    }
}
