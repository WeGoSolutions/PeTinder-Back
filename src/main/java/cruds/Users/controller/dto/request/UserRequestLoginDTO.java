package cruds.Users.controller.dto.request;

import cruds.Users.entity.User;
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
    private String email;

    @NotBlank
    private String senha;
}
