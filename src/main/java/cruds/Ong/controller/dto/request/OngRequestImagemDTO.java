package cruds.Ong.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OngRequestImagemDTO {

    @NotBlank
    private String imagensBytes;
    private String nomeArquivo;

    public byte[] getImagensBytesDecoded() {
        String base64Data = imagensBytes;
        if (base64Data.contains(",")) {
            base64Data = base64Data.substring(base64Data.indexOf(",") + 1);
        }
        return Base64.getDecoder().decode(base64Data);
    }

}
