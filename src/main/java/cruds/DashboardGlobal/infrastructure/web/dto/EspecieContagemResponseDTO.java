package cruds.DashboardGlobal.infrastructure.web.dto;

import cruds.DashboardGlobal.core.domain.EspecieContagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EspecieContagemResponseDTO {

    private String especie;
    private long quantidade;

    public static EspecieContagemResponseDTO fromDomain(EspecieContagem domain) {
        return new EspecieContagemResponseDTO(domain.getEspecie(), domain.getQuantidade());
    }
}
