package cruds.DashboardGlobal.infrastructure.persistence.jpa;

import cruds.DashboardGlobal.core.adapter.DadosGoldGateway;
import cruds.DashboardGlobal.core.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DadosGoldJpaAdapter implements DadosGoldGateway {

    private final DadosGoldJpaRepository repository;

    public DadosGoldJpaAdapter(DadosGoldJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public long contarTotalEntradas() {
        return repository.contarTotal();
    }

    @Override
    public long contarAdotados() {
        return repository.contarAdotados();
    }

    @Override
    public double calcularTempoMedioAdocao() {
        return repository.calcularTempoMedioAdocao();
    }

    @Override
    public long contarDevolucoes() {
        return repository.contarDevolucoes();
    }

    @Override
    public long contarAdocoes() {
        return repository.contarAdocoes();
    }

    @Override
    public List<EspecieContagem> listarTopEspecies(int limit) {
        return repository.listarTopEspecies().stream()
                .map(row -> new EspecieContagem(
                        (String) row[0],
                        ((Number) row[1]).longValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<StatusContagem> listarDistribuicaoStatus() {
        return repository.listarDistribuicaoStatus().stream()
                .map(row -> new StatusContagem(
                        (String) row[0],
                        ((Number) row[1]).longValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MotivoRetornoContagem> listarTopMotivosRetorno(int limit) {
        return repository.listarTopMotivosRetorno().stream()
                .map(row -> new MotivoRetornoContagem(
                        (String) row[0],
                        ((Number) row[1]).longValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<FaixaEtariaContagem> listarDistribuicaoFaixaEtaria() {
        return repository.listarDistribuicaoFaixaEtaria().stream()
                .map(row -> new FaixaEtariaContagem(
                        (String) row[0],
                        ((Number) row[1]).longValue()))
                .collect(Collectors.toList());
    }
}
