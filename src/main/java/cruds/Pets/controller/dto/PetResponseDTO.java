package cruds.Pets.controller.dto;

import cruds.Pets.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponseDTO {

    private String nome;
    private Double idade;
    private Integer curtidas;

    public static PetResponseDTO toResponse(Pet pet) {
        return PetResponseDTO.builder()
                .nome(pet.getNome())
                .idade(pet.getIdade())
                .curtidas(pet.getCurtidas())
                .build();
    }

}
