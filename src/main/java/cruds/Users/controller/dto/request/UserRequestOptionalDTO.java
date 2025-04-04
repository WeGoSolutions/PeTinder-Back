package cruds.Users.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestOptionalDTO {

    @Pattern(regexp = "\\d{11}")
    private String cpf;

    @Pattern(regexp = "\\d{8}")
    private String cep;

    @NotBlank
    private String rua;

    @NotBlank
    private Integer numero;

    private String complemento;

    @NotBlank
    private String cidade;

    @NotBlank
    private String uf;


    public static UserRequestOptionalDTO toOptional(@Valid UserRequestOptionalDTO user) {
        UserRequestOptionalDTO dto = new UserRequestOptionalDTO();
        dto.setCpf(user.getCpf());
        dto.setCep(user.getCep());
        dto.setRua(user.getRua());
        dto.setNumero(user.getNumero());
        dto.setComplemento(user.getComplemento());
        dto.setCidade(user.getCidade());
        dto.setUf(user.getUf());
        return dto;
    }

}
