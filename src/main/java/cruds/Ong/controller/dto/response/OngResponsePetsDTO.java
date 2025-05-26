package cruds.Ong.controller.dto.response;

import cruds.Imagem.entity.Imagem;
import cruds.Pets.entity.Pet;
import cruds.Pets.entity.PetStatus;
import jakarta.persistence.ElementCollection;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Builder
@Data
@Getter
@Setter
public class OngResponsePetsDTO {
    private Integer ongId;
    private Integer petId;
    private String petNome;
    private Double idade;
    private Double peso;
    private Double altura;
    private Integer curtidas;

    @ElementCollection
    private List<String> tags;

    private String descricao;
    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;
    private String status;
    private List<String> imageUrl;
    private String sexo;

    public OngResponsePetsDTO() {}

    public OngResponsePetsDTO(Integer ongId, Pet pet) {
        this.ongId = ongId;
        this.petId = pet.getId();
        this.petNome = pet.getNome();
        this.idade = pet.getIdade();
        this.peso = pet.getPeso();
        this.altura = pet.getAltura();
        this.curtidas = pet.getCurtidas();
        this.tags = pet.getTags();
        this.descricao = pet.getDescricao();
        this.isCastrado = pet.getIsCastrado();
        this.isVermifugo = pet.getIsVermifugo();
        this.isVacinado = pet.getIsVacinado();
        this.status = pet.getStatus() != null ? pet.getStatus().toString() : null;
        this.sexo = pet.getSexo();

        String baseUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();

        this.imageUrl = pet.getImagens() == null
                ? null
                : IntStream.range(0, pet.getImagens().size())
                .mapToObj(i -> baseUri + "/pets/" + pet.getId() + "/imagens/" + i)
                .collect(Collectors.toList());
    }

    public OngResponsePetsDTO(Integer ongId,
                              Integer petId,
                              String petNome,
                              Double idade,
                              Double peso,
                              Double altura,
                              Integer curtidas,
                              List<String> tags,
                              String descricao,
                              Boolean isCastrado,
                              Boolean isVermifugo,
                              Boolean isVacinado,
                              String status,
                              List<String> imageUrl,
                              String sexo) {
        this.ongId = ongId;
        this.petId = petId;
        this.petNome = petNome;
        this.idade = idade;
        this.peso = peso;
        this.altura = altura;
        this.curtidas = curtidas;
        this.tags = tags;
        this.descricao = descricao;
        this.isCastrado = isCastrado;
        this.isVermifugo = isVermifugo;
        this.isVacinado = isVacinado;
        this.status = status;
        this.imageUrl = imageUrl;
        this.sexo = sexo;
    }
}
