package cruds.Ong.controller.dto.response;

import cruds.Ong.entity.Ong;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OngResponseDTO {

    private Integer id;
    private String cnpj;
    private String cpf;
    private String nome;
    private String razaoSocial;
    private String email;
    private String senha;
    private String link;

    public static OngResponseDTO toResponse(Ong ong) {
        return OngResponseDTO.builder()
                .id(ong.getId())
                .cnpj(ong.getCnpj())
                .nome(ong.getNome())
                .razaoSocial(ong.getRazaoSocial())
                .email(ong.getEmail())
                .build();
    }

}
