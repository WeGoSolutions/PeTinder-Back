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
    private byte[] imagensBytes;
    private String nomeArquivo;

}
