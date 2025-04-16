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
    @Pattern(regexp = "^[A-Za-z\\s]+$")
    private String rua;

    @NotBlank
    private Integer logradouro;

    @Pattern(regexp = "^[A-Za-z\\s]+$")
    private String complemento;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z\\s]+$")
    private String cidade;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z\\s]+$")
    private String uf;
}
