package cruds.Pets.V2.infrastructure.web.dto;

import cruds.Pets.V2.core.domain.Pet;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponseWebDTO {

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
    private Boolean isAdotado;
    private String sexo;
    private UUID ongId;
    private LocalDateTime dataCriacao;
    private List<String> imagensUrls;
    private Integer totalImagens;
    private PetStatusEnum statusUsuario; // Novo campo para o status do pet com o usuário

    public static PetResponseWebDTO fromDomain(Pet pet) {
        List<String> imagensUrls = null;
        int totalImagens = 0;

        if (pet.getImagens() != null) {
            totalImagens = pet.getImagens().size();
            imagensUrls = pet.getImagens().stream()
                    .map(imagem -> "/pets/" + pet.getId() + "/imagens/" +
                         pet.getImagens().indexOf(imagem))
                    .toList();
        }
        
        return PetResponseWebDTO.builder()
                .id(pet.getId())
                .nome(pet.getNome())
                .idade(pet.getIdade())
                .porte(pet.getPorte())
                .curtidas(pet.getCurtidas())
                .tags(pet.getTags())
                .descricao(pet.getDescricao())
                .isCastrado(pet.getIsCastrado())
                .isVermifugo(pet.getIsVermifugo())
                .isVacinado(pet.getIsVacinado())
                .isAdotado(pet.getIsAdotado())
                .sexo(pet.getSexo())
                .ongId(pet.getOngId())
                .dataCriacao(pet.getDataCriacao())
                .imagensUrls(imagensUrls)
                .totalImagens(totalImagens)
                .statusUsuario(null) // Será preenchido no controller quando necessário
                .build();
    }

    // Método auxiliar para criar o DTO com status do usuário
    public static PetResponseWebDTO fromDomainWithUserStatus(Pet pet, PetStatusEnum statusUsuario) {
        PetResponseWebDTO dto = fromDomain(pet);
        dto.setStatusUsuario(statusUsuario);
        return dto;
    }
}