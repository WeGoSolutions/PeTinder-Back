package cruds.Users.controller.dto.request;

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
public class UserRequestImagemPerfilDTO {
    @NotBlank
    private String imagemUsuario;

    public byte[] getImagemDecodificada() {
        return Base64.getDecoder().decode(imagemUsuario);
    }
}