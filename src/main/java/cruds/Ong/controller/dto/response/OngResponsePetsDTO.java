package cruds.Ong.controller.dto.response;

import cruds.Imagem.entity.Imagem;
import cruds.Pets.entity.Pet;
import cruds.Pets.entity.PetStatus;
import jakarta.persistence.ElementCollection;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Builder
@Data
@Getter
@Setter
public class OngResponsePetsDTO {
    private UUID ongId;
    private UUID petId;
    private String petNome;
    private Double idade;
    private String porte;
    private Integer curtidas;

    @ElementCollection
    private List<String> tags;

    private String descricao;
    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;
    private List<String> imageUrl;
    private String sexo;
    private List<String> status; // Lista de status do pet

    public OngResponsePetsDTO() {}

    // Construtor que recebe a lista de status como parâmetro
    public OngResponsePetsDTO(UUID ongId, Pet pet, List<String> statusList) {
        this.ongId = ongId;
        this.petId = pet.getId();
        this.petNome = pet.getNome();
        this.idade = pet.getIdade();
        this.porte = pet.getPorte();
        this.curtidas = pet.getCurtidas();
        this.tags = pet.getTags();
        this.descricao = pet.getDescricao();
        this.isCastrado = pet.getIsCastrado();
        this.isVermifugo = pet.getIsVermifugo();
        this.isVacinado = pet.getIsVacinado();
        this.sexo = pet.getSexo();
        this.status = statusList;

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

    public OngResponsePetsDTO(UUID ongId, Pet pet) {
        this(ongId, pet, null);
    }

    // Construtor padrão sem status (mantido para compatibilidade)
    public OngResponsePetsDTO(UUID ongId,
                              UUID petId,
                              String petNome,
                              Double idade,
                              String porte,
                              Integer curtidas,
                              List<String> tags,
                              String descricao,
                              Boolean isCastrado,
                              Boolean isVermifugo,
                              Boolean isVacinado,
                              List<String> imageUrl,
                              String sexo) {
        this.ongId = ongId;
        this.petId = petId;
        this.petNome = petNome;
        this.idade = idade;
        this.porte = porte;
        this.curtidas = curtidas;
        this.tags = tags;
        this.descricao = descricao;
        this.isCastrado = isCastrado;
        this.isVermifugo = isVermifugo;
        this.isVacinado = isVacinado;
        this.imageUrl = imageUrl;
        this.sexo = sexo;
    }

    // Novo construtor para aceitar o parâmetro extra da lista de status (15 argumentos)
    public OngResponsePetsDTO(UUID ongId,
                              UUID petId,
                              String petNome,
                              Double idade,
                              String porte,
                              Integer curtidas,
                              List<String> tags,
                              String descricao,
                              Boolean isCastrado,
                              Boolean isVermifugo,
                              Boolean isVacinado,
                              List<String> imageUrl,
                              String sexo,
                              List<String> status) {
        this.ongId = ongId;
        this.petId = petId;
        this.petNome = petNome;
        this.idade = idade;
        this.porte = porte;
        this.curtidas = curtidas;
        this.tags = tags;
        this.descricao = descricao;
        this.isCastrado = isCastrado;
        this.isVermifugo = isVermifugo;
        this.isVacinado = isVacinado;
        this.imageUrl = imageUrl;
        this.sexo = sexo;
        this.status = status;
    }
}