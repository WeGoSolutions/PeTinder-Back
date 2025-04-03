package cruds.Pets.controller.dto;

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
public class PetResponseDTO {

    private String nome;
    private Double idade;
    private Integer curtidas;
    private String descricao;
    private List<String> tags;

    public static PetResponseDTO toResponse(Pet pet) {
        return PetResponseDTO.builder()
                .nome(pet.getNome())
                .idade(pet.getIdade())
                .curtidas(pet.getCurtidas())
                .descricao(pet.getDescricao())
                .tags(pet.getTags())
                .build();
    }
}