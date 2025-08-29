package cruds.Users.V2.infrastructure.web.dto;

import cruds.Users.V2.core.application.command.AtualizarSenhaCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para atualização de senha - Web Layer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarSenhaWebDTO {

    @NotBlank(message = "Senha atual é obrigatória")
    @Schema(description = "Senha atual do usuário", example = "Urubu@123")
    private String senhaAtual;

    @NotBlank(message = "Nova senha é obrigatória")
    @Size(min = 8, message = "Nova senha deve ter pelo menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\\\":{}|<>]+$", 
             message = "Nova senha deve conter pelo menos: uma letra maiúscula, uma minúscula, um número e um caractere especial")
    @Schema(description = "Nova senha do usuário", example = "NovaUrubu@456")
    private String novaSenha;

    public AtualizarSenhaCommand toCommand(Long usuarioId) {
        return new AtualizarSenhaCommand(usuarioId, senhaAtual, novaSenha);
    }
}
