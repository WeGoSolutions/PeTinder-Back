package cruds.DashboardGlobal.infrastructure.web.dto;

import cruds.DashboardGlobal.core.domain.MotivoRetornoContagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MotivoRetornoContagemResponseDTO {

    private String motivo;
    private long quantidade;

    public static MotivoRetornoContagemResponseDTO fromDomain(MotivoRetornoContagem domain) {
        return new MotivoRetornoContagemResponseDTO(domain.getMotivo(), domain.getQuantidade());
    }
}
