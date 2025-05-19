package cruds.Ong.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OngResponseLoginDTO {

    private Integer id;
    private String nome;
    private String email;

}
