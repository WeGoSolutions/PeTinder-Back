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
        String base64Data = imagemUsuario;
        if (base64Data.startsWith("data:")) {
            int commaIndex = base64Data.indexOf(",");
            if (commaIndex != -1) {
                base64Data = base64Data.substring(commaIndex + 1);
            }
        }
        return Base64.getDecoder().decode(base64Data);
    }
}