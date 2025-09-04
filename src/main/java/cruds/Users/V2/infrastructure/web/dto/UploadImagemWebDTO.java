package cruds.Users.V2.infrastructure.web.dto;

import cruds.Users.V2.core.application.command.UploadImagemCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadImagemWebDTO {

    @NotNull(message = "Dados da imagem são obrigatórios")
    @Schema(description = "Dados da imagem em Base64", example = "iVBORw0KGgoAAAANSUhEUgAA...")
    private byte[] dadosImagem;

    @Schema(description = "Nome do arquivo", example = "perfil.jpg")
    private String nomeArquivo;

    public UploadImagemCommand toCommand(UUID usuarioId) {
        return new UploadImagemCommand(usuarioId, dadosImagem, nomeArquivo);
    }
}
