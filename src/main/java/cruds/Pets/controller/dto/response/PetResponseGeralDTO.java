package cruds.Pets.controller.dto.response;

import cruds.Pets.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponseGeralDTO {
    private Integer id;
    private String nome;
    private Double idade;
    private Integer curtidas;
    private String descricao;
    private List<String> tags;
    private Boolean isLiked;
    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;
    private Boolean isAdotado;

    public static PetResponseGeralDTO toResponse(Pet pet) {
        return PetResponseGeralDTO.builder()
                .id(pet.getId())
                .nome(pet.getNome())
                .idade(pet.getIdade())
                .curtidas(pet.getCurtidas())
                .descricao(pet.getDescricao())
                .tags(pet.getTags())
                .isLiked(pet.getIsLiked())
                .isCastrado(pet.getIsCastrado())
                .isVacinado(pet.getIsVacinado())
                .isVermifugo(pet.getIsVermifugo())
                .isAdotado(pet.getIsAdotado())
                .build();
    }
}
