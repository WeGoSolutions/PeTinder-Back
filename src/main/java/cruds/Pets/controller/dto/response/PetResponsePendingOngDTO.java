package cruds.Pets.controller.dto.response;

import cruds.Ong.controller.dto.response.OngResponseDTO;
import cruds.Pets.entity.Pet;
import cruds.Pets.entity.PetStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetResponsePendingOngDTO {
    private Integer userId;
    private Integer petId;
    private String petNome;
    private Double idade;
    private Double peso;
    private Double altura;
    private String descricao;
    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;
    private List<String> imageUrl;
    private String sexo;
    private Integer ongId;
    private OngResponseDTO ongInfo;
}
