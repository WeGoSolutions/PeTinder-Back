package cruds.DashboardGlobal.infrastructure.web.dto;

import cruds.DashboardGlobal.core.domain.DashboardGlobalKpis;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardGlobalKpisResponseDTO {

    private long totalEntradas;
    private double taxaAdocao;
    private double tempoMedioAdocao;
    private double taxaRetorno;

    public static DashboardGlobalKpisResponseDTO fromDomain(DashboardGlobalKpis kpis) {
        return new DashboardGlobalKpisResponseDTO(
                kpis.getTotalEntradas(),
                Math.round(kpis.getTaxaAdocao() * 100.0) / 100.0,
                Math.round(kpis.getTempoMedioAdocao() * 10.0) / 10.0,
                Math.round(kpis.getTaxaRetorno() * 100.0) / 100.0
        );
    }
}
