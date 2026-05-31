package cruds.DashboardGlobal.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DadosGoldJpaRepository extends JpaRepository<DadosGoldEntity, Integer> {

    @Query(value = "SELECT COUNT(*) FROM dados_gold", nativeQuery = true)
    long contarTotal();

    @Query(value = "SELECT COUNT(*) FROM dados_gold WHERE foi_adotado = 'Sim'", nativeQuery = true)
    long contarAdotados();

    @Query(value = "SELECT COALESCE(AVG(dias_no_abrigo), 0) FROM dados_gold WHERE foi_adotado = 'Sim' AND dias_no_abrigo IS NOT NULL", nativeQuery = true)
    double calcularTempoMedioAdocao();

    @Query(value = "SELECT COUNT(*) FROM dados_gold WHERE abandonado_devolvido LIKE '%Devolu\u00e7\u00e3o%'", nativeQuery = true)
    long contarDevolucoes();

    @Query(value = "SELECT COUNT(*) FROM dados_gold WHERE tipo_movimentacao = 'Ado\u00e7\u00e3o'", nativeQuery = true)
    long contarAdocoes();

    @Query(value = "SELECT especie, COUNT(*) as quantidade FROM dados_gold WHERE especie IS NOT NULL GROUP BY especie ORDER BY quantidade DESC LIMIT 5", nativeQuery = true)
    List<Object[]> listarTopEspecies();

    @Query(value = "SELECT status_final, COUNT(*) as quantidade FROM dados_gold WHERE status_final IS NOT NULL GROUP BY status_final ORDER BY quantidade DESC", nativeQuery = true)
    List<Object[]> listarDistribuicaoStatus();

    @Query(value = "SELECT abandonado_devolvido, COUNT(*) as quantidade FROM dados_gold WHERE abandonado_devolvido LIKE '%Devolu\u00e7\u00e3o%' GROUP BY abandonado_devolvido ORDER BY quantidade DESC LIMIT 5", nativeQuery = true)
    List<Object[]> listarTopMotivosRetorno();

    @Query(value = "SELECT faixa_etaria, COUNT(*) as quantidade FROM dados_gold WHERE faixa_etaria IS NOT NULL GROUP BY faixa_etaria ORDER BY quantidade DESC", nativeQuery = true)
    List<Object[]> listarDistribuicaoFaixaEtaria();
}
