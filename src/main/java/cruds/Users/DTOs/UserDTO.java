package cruds.Users.DTOs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String nome;

    private String email;

    private String senha;

    private Date dataNasc;

    private String cpf;

    private String cep;

    private String rua;

    private Integer numero;

    private String cidade;

    private String uf;

}