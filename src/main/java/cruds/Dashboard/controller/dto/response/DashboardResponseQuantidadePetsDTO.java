package cruds.Dashboard.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardResponseQuantidadePetsDTO {
    private int adotados;
    private int naoAdotados;
}