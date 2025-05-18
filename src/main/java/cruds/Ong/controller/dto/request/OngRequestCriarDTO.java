package cruds.Ong.controller.dto.request;

import cruds.Ong.controller.dto.response.OngResponseDTO;
import cruds.Ong.entity.Ong;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OngRequestCriarDTO {

    @Pattern(regexp = "\\d{14}")
    private String cnpj;

    @Pattern(regexp = "\\d{11}")
    private String cpf;

    @NotBlank
    @Size(min = 3)
    @Pattern(regexp = "^[A-Za-zÀ-Ö ]+$")
    private String nome;

    @NotBlank
    private String razaoSocial;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\\\":{}|<>]+$")
    private String senha;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String link;

    public static Ong toEntity(OngRequestCriarDTO ongRequest) {
        return Ong.builder()
                .cnpj(ongRequest.getCnpj())
                .cpf(ongRequest.getCpf())
                .nome(ongRequest.getNome())
                .razaoSocial(ongRequest.getRazaoSocial())
                .senha(ongRequest.getSenha())
                .email(ongRequest.getEmail())
                .link(ongRequest.getLink())
                .build();
    }


}
