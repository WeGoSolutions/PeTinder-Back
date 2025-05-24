package cruds.Pets.controller.dto.response;

import cruds.Pets.entity.Pet;
import cruds.Imagem.entity.Imagem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponseGeralDTO {
    private Integer id;
    private String nome;
    private Double idade;
    private Double peso;
    private Double altura;
    private Integer curtidas;
    private String descricao;
    private List<String> tags;
    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;
    private  String status;
    private List<String> imagens;

    public PetResponseGeralDTO(Pet pet) {
        this.id = pet.getId();
        this.nome = pet.getNome();
        this.idade = pet.getIdade();
        this.peso = pet.getPeso();
        this.altura = pet.getAltura();
        this.curtidas = pet.getCurtidas();
        this.descricao = pet.getDescricao();
        this.tags = pet.getTags();
        this.isCastrado = pet.getIsCastrado();
        this.isVermifugo = pet.getIsVermifugo();
        this.isVacinado = pet.getIsVacinado();
        this.status = pet.getStatus();

        if (pet.getImagens() != null) {
            this.imagens = pet.getImagens().stream()
                    .map(Imagem::getCaminho)
                    .collect(Collectors.toList());
        }
    }

    public static PetResponseGeralDTO toResponse(Pet pet) {
        List<String> imagemUrls = pet.getImagens() == null
                ? null
                : pet.getImagens().stream()
                .map(Imagem::getCaminho)
                .collect(Collectors.toList());

        return PetResponseGeralDTO.builder()
                .id(pet.getId())
                .nome(pet.getNome())
                .idade(pet.getIdade())
                .peso(pet.getPeso())
                .altura(pet.getAltura())
                .curtidas(pet.getCurtidas())
                .descricao(pet.getDescricao())
                .tags(pet.getTags())
                .isCastrado(pet.getIsCastrado())
                .isVermifugo(pet.getIsVermifugo())
                .isVacinado(pet.getIsVacinado())
                .imagens(imagemUrls)
                .status(pet.getStatus())
                .build();
    }
}