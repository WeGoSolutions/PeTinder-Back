package cruds.Ong.V2.infrastructure.web.dto;

import cruds.Ong.V2.core.domain.Ong;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OngUrlResponseWebDTO {

    private UUID id;
    private String url;

    public static OngUrlResponseWebDTO fromDomain(Ong ong) {
        String url = null;
        if (ong.getImagemOng() != null && ong.getImagemOng().getArquivo() != null) {
            url = ong.getImagemOng().getArquivo();
        }

        return OngUrlResponseWebDTO.builder()
            .id(ong.getId())
            .url(url)
            .build();
    }
}

