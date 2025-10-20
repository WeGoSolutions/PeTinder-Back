package cruds.Ong.V2.infrastructure.web.dto;

import cruds.Ong.V2.core.domain.Ong;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OngUrlResponseWebDTO {

    private UUID id;
    private String url;

    public static OngUrlResponseWebDTO fromDomain(Ong ong) {
        OngUrlResponseWebDTO dto = OngUrlResponseWebDTO.builder()
                .id(ong.getId())
                .url(null)
                .build();

        if (ong.getImagemOng() != null && ong.getImagemOng().temImagem()) {
            String base64Image = Base64.getEncoder().encodeToString(ong.getImagemOng().getDados());
            dto.setUrl("data:image/jpeg;base64," + base64Image);
        }

        return dto;
    }
}

