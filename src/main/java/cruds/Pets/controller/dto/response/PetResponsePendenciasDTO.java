package cruds.Pets.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PetResponsePendenciasDTO {
    private String nome;
    private List<String> pendencias;
}
