package cruds.DashboardGlobal.core.adapter;

import cruds.DashboardGlobal.core.domain.*;

import java.util.List;

public interface DadosGoldGateway {

    long contarTotalEntradas();

    long contarAdotados();

    double calcularTempoMedioAdocao();

    long contarDevolucoes();

    long contarAdocoes();

    List<EspecieContagem> listarTopEspecies(int limit);

    List<StatusContagem> listarDistribuicaoStatus();

    List<MotivoRetornoContagem> listarTopMotivosRetorno(int limit);

    List<FaixaEtariaContagem> listarDistribuicaoFaixaEtaria();
}
