package cruds.Pets.controller.dto.response;

import cruds.Pets.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponseCriarDTO {

    private Integer id;
    private String nome;
    private Double idade;
    private Integer curtidas;
    private String descricao;
    private List<String> tags;
    private List<String> imagens;
    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;
    private Integer ongId;
    private String status;

    public static PetResponseCriarDTO toResponse(Pet pet) {
        return PetResponseCriarDTO.builder()
                .id(pet.getId())
                .nome(pet.getNome())
                .idade(pet.getIdade())
                .curtidas(pet.getCurtidas())
                .descricao(pet.getDescricao())
                .tags(pet.getTags())
                .imagens(Collections.emptyList())
                .isCastrado(pet.getIsCastrado())
                .isVacinado(pet.getIsVacinado())
                .isVermifugo(pet.getIsVermifugo())
                .ongId(pet.getOng().getId())
                .status(pet.getStatus())
                .build();
    }
}