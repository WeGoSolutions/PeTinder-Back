package cruds.Ong.controller.dto.response;

import cruds.Ong.entity.Ong;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OngResponseUrlDTO {
    private Integer id;
    private String nome;
    private String email;
    private String imageUrl;

    public static OngResponseUrlDTO toResponse(Ong ong) {
        return OngResponseUrlDTO.builder()
                .id(ong.getId())
                .nome(ong.getNome())
                .email(ong.getEmail())
                .imageUrl(ong.getImagemOng() != null ? ong.getImagemOng().getArquivo() : null)
                .build();
    }
}
