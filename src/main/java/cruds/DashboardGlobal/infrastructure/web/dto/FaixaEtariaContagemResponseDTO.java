package cruds.DashboardGlobal.infrastructure.web.dto;

import cruds.DashboardGlobal.core.domain.FaixaEtariaContagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaixaEtariaContagemResponseDTO {

    private String faixaEtaria;
    private long quantidade;

    public static FaixaEtariaContagemResponseDTO fromDomain(FaixaEtariaContagem domain) {
        return new FaixaEtariaContagemResponseDTO(domain.getFaixaEtaria(), domain.getQuantidade());
    }
}
