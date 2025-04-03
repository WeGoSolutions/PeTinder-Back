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
public class PetResponseCriarDTO {

    private String nome;
    private Double idade;
    private Integer curtidas;
    private String descricao;
    private List<String> tags;
    private Boolean isLiked;

    public static PetResponseCriarDTO toResponse(Pet pet) {
        return PetResponseCriarDTO.builder()
                .nome(pet.getNome())
                .idade(pet.getIdade())
                .curtidas(pet.getCurtidas())
                .descricao(pet.getDescricao())
                .tags(pet.getTags())
                .isLiked(false)
                .build();
    }
}