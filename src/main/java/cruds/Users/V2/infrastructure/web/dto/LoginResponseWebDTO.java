package cruds.Users.V2.infrastructure.web.dto;

import cruds.Users.V2.core.application.usecase.LoginUsuarioUseCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de response para login - Web Layer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseWebDTO {
    private UsuarioResponseWebDTO usuario;
    private String token;

    public static LoginResponseWebDTO fromResult(LoginUsuarioUseCase.LoginResult result) {
        return LoginResponseWebDTO.builder()
                .usuario(UsuarioResponseWebDTO.fromDomain(result.getUsuario()))
                .token(result.getToken())
                .build();
    }
}
