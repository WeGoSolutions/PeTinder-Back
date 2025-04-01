package cruds.Pets.dto;

import cruds.Pets.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponse {

    private String nome;
    private Double idade;
    private Integer curtidas;

    public static PetResponse toResponse(Pet pet) {
        return PetResponse.builder()
                .nome(pet.getNome())
                .idade(pet.getIdade())
                .curtidas(pet.getCurtidas())
                .build();
    }

}
