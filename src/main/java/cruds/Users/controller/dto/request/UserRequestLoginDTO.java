package cruds.Users.controller.dto.request;

import cruds.Users.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Base64;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserRequestLoginDTO {

    @Email
    @NotBlank
    @Schema(description = "Email do usuário", example = "petinder@gmail.com")
    private String email;

    @NotBlank
    @Schema(description = "Senha do usuário", example = "Urubu@123")
    private String senha;

}
