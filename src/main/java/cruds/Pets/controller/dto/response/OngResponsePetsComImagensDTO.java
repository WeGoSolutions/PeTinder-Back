package cruds.Pets.controller.dto.response;

import cruds.Pets.entity.Pet;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OngResponsePetsComImagensDTO {
    private UUID id;
    private String nome;
    private Double idade;
    private String porte;
    private Integer curtidas;
    private List<String> tags;
    private String descricao;
    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;
    private Boolean isAdopted;
    private String sexo;
    private List<String> statusList;
    private List<String> imagensUrls;

    public OngResponsePetsComImagensDTO(UUID ongId, Pet pet, List<String> statusList, List<String> imagensUrls) {
        this.id = pet.getId();
        this.nome = pet.getNome();
        this.idade = pet.getIdade();
        this.porte = pet.getPorte();
        this.curtidas = pet.getCurtidas();
        this.tags = pet.getTags();
        this.descricao = pet.getDescricao();
        this.isCastrado = pet.getIsCastrado();
        this.isVermifugo = pet.getIsVermifugo();
        this.isVacinado = pet.getIsVacinado();
        this.isAdopted = pet.getIsAdopted();
        this.sexo = pet.getSexo();
        this.statusList = statusList;
        this.imagensUrls = imagensUrls;
    }
}