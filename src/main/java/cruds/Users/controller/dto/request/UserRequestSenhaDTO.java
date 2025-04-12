package cruds.Users.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRequestSenhaDTO {

    @NotBlank
    @Size(min = 6)
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z0-9!@#$%^&*(),.?\\\":{}|<>]+$")
    private String senha;

    public @NotBlank @Size(min = 6) @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z0-9!@#$%^&*(),.?\\\":{}|<>]+$") String getSenha() {
        return senha;
    }

    public void setSenha(@NotBlank @Size(min = 6) @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z0-9!@#$%^&*(),.?\\\":{}|<>]+$") String senha) {
        this.senha = senha;
    }
}
