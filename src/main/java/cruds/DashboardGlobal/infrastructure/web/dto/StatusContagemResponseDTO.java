package cruds.DashboardGlobal.infrastructure.web.dto;

import cruds.DashboardGlobal.core.domain.StatusContagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusContagemResponseDTO {

    private String status;
    private long quantidade;

    public static StatusContagemResponseDTO fromDomain(StatusContagem domain) {
        return new StatusContagemResponseDTO(domain.getStatus(), domain.getQuantidade());
    }
}
