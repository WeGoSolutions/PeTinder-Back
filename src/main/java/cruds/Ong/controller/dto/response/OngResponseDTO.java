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

    private String cnpj;
    private String nome;
    private String razaoSocial;
    private String email;

    public static OngResponseDTO toResponse(Ong ong) {
        return OngResponseDTO.builder()
                .cnpj(ong.getCnpj())
                .nome(ong.getNome())
                .razaoSocial(ong.getRazaoSocial())
                .email(ong.getEmail())
                .build();
    }

}
