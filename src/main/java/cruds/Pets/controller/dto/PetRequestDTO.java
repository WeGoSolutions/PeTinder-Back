package cruds.Pets.controller.dto;

import cruds.Pets.entity.Pet;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetRequestDTO {

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
    private String descricao;

    private Boolean isLiked;

    @Max(value = 5)
    @NotBlank
    private List<String> imagemBase64;

    public static Pet toEntity(PetRequestDTO petRequest) {
        return Pet.builder()
                .nome(petRequest.getNome())
                .idade(petRequest.getIdade())
                .peso(petRequest.getPeso())
                .altura(petRequest.getAltura())
                .curtidas(petRequest.getCurtidas())
                .tags(petRequest.getTags())
                .descricao(petRequest.getDescricao())
                .isLiked(petRequest.getIsLiked())
                .build();
    }
}