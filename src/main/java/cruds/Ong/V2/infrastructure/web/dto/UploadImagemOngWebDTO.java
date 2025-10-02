package cruds.Ong.V2.infrastructure.web.dto;

import cruds.Ong.V2.core.application.command.UploadImagemOngCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadImagemOngWebDTO {

    @NotBlank
    private String imagem;

    public UploadImagemOngCommand toCommand(UUID ongId) {
        byte[] imagemBytes = Base64.getDecoder().decode(imagem);
        return new UploadImagemOngCommand(ongId, imagemBytes);
    }

    public byte[] getImagensBytesDecoded() {
        return Base64.getDecoder().decode(imagem);
    }
}

