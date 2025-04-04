package cruds.Users.controller.dto.request;

import cruds.Users.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestCriarDTO {

    @NotBlank
    @Size(min = 3)
    @Pattern(regexp = "^[A-Za-zÀ-Ö ]+$")
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z0-9!@#$%^&*(),.?\":{}|<>]+$")
    private String senha;

    @Past
    private Date dataNasc;

//    elas podem estar vazias se forem preenchidas depois utiliza UserOptional
//    é no UserOptional em que as validações são feitas
//    private String cpf;
//    private String cep;
//    private String rua;
//    private Integer numero;
//    private String complemento;
//    private String cidade;
//    private String uf;

    @AssertTrue(message = "A pessoa deve ter mais de 21 anos")
    public boolean isMaiorDe21() {
        if (dataNasc == null) {
            return false;
        }

        LocalDate dataNascimento = new java.sql.Date(dataNasc.getTime()).toLocalDate();
        LocalDate hoje = LocalDate.now();
        Period periodo = Period.between(dataNascimento, hoje);

        return periodo.getYears() >= 21;
    }

    public static User toEntity(@Valid UserRequestCriarDTO usuario){
        return User.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .dataNasc(usuario.getDataNasc())
//                .cpf(usuario.getCpf())
//                .cep(usuario.getCep())
//                .rua(usuario.getRua())
//                .numero(usuario.getNumero())
//                .complemento(usuario.getComplemento())
//                .cidade(usuario.getCidade())
//                .uf(usuario.getUf())
                .build();
    }

}
