package cruds.Users.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class UserRequestSenhaDTO {

    @NotBlank
    @Size(min = 6)
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z0-9!@#$%^&*(),.?\\\":{}|<>]+$")
    @Schema(description = "Senha do usuário", example = "Urubu@123")
    private String senha;

    @NotBlank
    @Email
    @Schema(description = "Email do usuário", example = "petinder@gmail.com")
    private String email;

    public @NotBlank @Size(min = 6) @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z0-9!@#$%^&*(),.?\\\":{}|<>]+$") String getSenha() {
        return senha;
    }
}
