package cruds.Pets.dto;

import cruds.Pets.entity.Pet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetRequest {

    @NotBlank
    private String nome;

    @Positive
    private Double idade;

    @NotBlank
    @Positive
    private Double peso;

    @NotBlank
    @Positive
    private Double altura;

    @PositiveOrZero
    private Integer curtidas;

    @NotEmpty
    private List<String> tags;

    @NotBlank
    private List<String> imagemBase64;

    public static Pet toEntity(PetRequest petRequest) {
        return Pet.builder()
                .nome(petRequest.getNome())
                .idade(petRequest.getIdade())
                .peso(petRequest.getPeso())
                .altura(petRequest.getAltura())
                .curtidas(petRequest.getCurtidas())
                .tags(petRequest.getTags())
                .build();
    }

}
