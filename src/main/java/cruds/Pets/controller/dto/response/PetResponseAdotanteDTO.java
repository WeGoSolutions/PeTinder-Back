package cruds.Pets.controller.dto.response;

import cruds.Pets.entity.Pet;
import cruds.Pets.entity.PetStatus;
import cruds.Users.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PetResponseAdotanteDTO {
    private UUID petId;
    private String nomePet;
    private Double idadePet;
    private String portePet;
    private Integer curtidas;
    private String descricao;
    private List<String> tags;
    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;
    private String sexoPet;
    private UUID userId;
    private UUID imagemUsuarioId;
    private String imagemUsuarioUrl;
    private String nomeUsuario;
    private String email;
    private LocalDate dataNascUsuario;
    private String cpf;

    public PetResponseAdotanteDTO(Pet pet, User user) {
        this.petId = pet.getId();
        this.nomePet = pet.getNome();
        this.idadePet = pet.getIdade();
        this.portePet = pet.getPorte();
        this.curtidas = pet.getCurtidas();
        this.descricao = pet.getDescricao();
        this.tags = pet.getTags();
        this.isCastrado = pet.getIsCastrado();
        this.isVermifugo = pet.getIsVermifugo();
        this.isVacinado = pet.getIsVacinado();
        this.sexoPet = pet.getSexo();
        this.userId = user.getId();
        this.imagemUsuarioId = user.getId();
        if (user.getImagemUser() != null) {
            this.imagemUsuarioUrl = "http://localhost:8080/users/" + user.getId() + "/imagens/0";
        }
        this.nomeUsuario = user.getNome();
        this.email = user.getEmail();
        this.dataNascUsuario = user.getDataNasc();
        this.cpf = user.getCpf();
    }
}
