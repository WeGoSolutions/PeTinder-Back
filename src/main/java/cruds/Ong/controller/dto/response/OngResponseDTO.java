package cruds.Ong.controller.dto.response;

import cruds.Ong.entity.Ong;
import cruds.Users.controller.dto.response.EnderecoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OngResponseDTO {

    private UUID id;
    private String cnpj;
    private String cpf;
    private String nome;
    private String razaoSocial;
    private String email;
    private String link;
    private EnderecoResponseDTO endereco;

    public static OngResponseDTO toResponse(Ong ong) {
        if (ong == null) return null;

        return OngResponseDTO.builder()
                .id(ong.getId())
                .nome(ong.getNome())
                .email(ong.getEmail())
                .cnpj(ong.getCnpj())
                .cpf(ong.getCpf())
                .razaoSocial(ong.getRazaoSocial())
                .link(ong.getLink())
                .endereco(EnderecoResponseDTO.toResponse(ong.getEndereco()))
                .build();
    }
}
