package cruds.Pets.controller.dto.request;

import cruds.Pets.entity.Pet;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PetRequestCriarDTO {

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

    @NotEmpty
    private Integer ongId;

    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;

    private Boolean isAdotado;

    private String status;

    @Size(max = 5)
    @NotEmpty
    private List<String> imagemBase64;

    public static Pet toEntity(PetRequestCriarDTO petRequest) {
        return Pet.builder()
                .nome(petRequest.getNome())
                .idade(petRequest.getIdade())
                .peso(petRequest.getPeso())
                .altura(petRequest.getAltura())
                .curtidas(0)
                .tags(petRequest.getTags())
                .descricao(petRequest.getDescricao())
                .isCastrado(Boolean.TRUE.equals(petRequest.getIsCastrado()))
                .isVermifugo(Boolean.TRUE.equals(petRequest.getIsVermifugo()))
                .isVacinado(Boolean.TRUE.equals(petRequest.getIsVacinado()))
                .isAdopted(false)
                .status("PENDING")
                .build();
    }
}