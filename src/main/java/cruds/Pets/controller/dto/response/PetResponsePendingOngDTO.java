package cruds.Pets.controller.dto.response;

import cruds.Ong.controller.dto.response.OngResponseDTO;
import cruds.Pets.entity.Pet;
import cruds.Pets.entity.PetStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetResponsePendingOngDTO {
    private UUID userId;
    private UUID petId;
    private String petNome;
    private Double idade;
    private String porte;
    private String descricao;
    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;
    private List<String> imageUrl;
    private String sexo;
    private UUID ongId;
    private OngResponseDTO ongInfo;
}
