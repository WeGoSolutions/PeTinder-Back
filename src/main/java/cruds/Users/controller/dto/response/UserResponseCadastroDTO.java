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
                .build();

        if (user.getEndereco() != null) {
            dto.setCep(user.getEndereco().getCep());
            dto.setRua(user.getEndereco().getRua());
            dto.setNumero(user.getEndereco().getNumero());
            dto.setCidade(user.getEndereco().getCidade());
            dto.setUf(user.getEndereco().getUf());
            dto.setComplemento(user.getEndereco().getComplemento());
        }

        if (user.getImagemUser() != null && user.getImagemUser().getDados() != null) {
            String base64Image = Base64.getEncoder().encodeToString(user.getImagemUser().getDados());
            dto.setImagemUrl("data:image/jpeg;base64," + base64Image);
        }

        return dto;
    }
}