package cruds.Ong.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OngRequestLoginDTO {

    @NotBlank
    private String senha;

    @Email
    @NotBlank
    private String email;

}
