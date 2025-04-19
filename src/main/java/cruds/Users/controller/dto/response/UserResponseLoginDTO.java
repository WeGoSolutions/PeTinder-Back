package cruds.Users.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserResponseLoginDTO {
    private String nome;
    private String email;
    private String token;
    private Boolean userNovo;
}