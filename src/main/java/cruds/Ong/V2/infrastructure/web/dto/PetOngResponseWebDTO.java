package cruds.Ong.V2.infrastructure.web.dto;

import cruds.Ong.V2.core.adapter.PetOngGateway;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetOngResponseWebDTO {

    private UUID id;
    private String nome;
    private String raca;
    private String porte;
    private Integer idade;
    private String sexo;
    private String descricao;
    private Boolean adotado;
    private List<String> status;

    public static PetOngResponseWebDTO fromPetInfo(PetOngGateway.PetOngInfo petInfo) {
        return PetOngResponseWebDTO.builder()
            .id(petInfo.getId())
            .nome(petInfo.getNome())
            .raca(petInfo.getRaca())
            .porte(petInfo.getPorte())
            .idade(petInfo.getIdade())
            .sexo(petInfo.getSexo())
            .descricao(petInfo.getDescricao())
            .adotado(petInfo.getAdotado())
            .status(petInfo.getStatus())
            .build();
    }
}

