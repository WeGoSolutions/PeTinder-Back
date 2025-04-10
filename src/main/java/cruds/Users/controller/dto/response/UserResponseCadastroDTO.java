package cruds.Users.controller.dto.response;

import cruds.Users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseCadastroDTO {
    private Integer id;
    private String nome;
    private String email;
    private Date dataNasc;
    private String cpf;
    private String cep;
    private String rua;
    private Integer numero;
    private String cidade;
    private String uf;
    private String complemento;
    private String imagemUrl;

    public static UserResponseCadastroDTO toResponse(User user) {
        UserResponseCadastroDTO dto = UserResponseCadastroDTO.builder()
                .id(user.getId())
                .nome(user.getNome())
                .email(user.getEmail())
                .dataNasc(user.getDataNasc())
                .cpf(user.getCpf())
                .cep(user.getCep())
                .rua(user.getRua())
                .numero(user.getNumero())
                .cidade(user.getCidade())
                .uf(user.getUf())
                .complemento(user.getComplemento())
                .build();

        if (user.getImagemUsuario() != null) {
            String base64Image = Base64.getEncoder().encodeToString(user.getImagemUsuario());
            dto.setImagemUrl("data:image/jpeg;base64," + base64Image);
        }

        return dto;
    }
}