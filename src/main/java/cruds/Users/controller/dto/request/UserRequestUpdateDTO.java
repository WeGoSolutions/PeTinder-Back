// linguagem: java
package cruds.Users.controller.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestUpdateDTO {

    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    @NotBlank
    private LocalDate dataNasc;

    @AssertTrue(message = "A pessoa deve ter mais de 21 anos")
    public boolean isMaiorDe21() {
        if (dataNasc == null) {
            return false;
        }
        LocalDate hoje = LocalDate.now();
        Period periodo = Period.between(dataNasc, hoje);
        return periodo.getYears() >= 21;
    }

    @NotBlank
    private String cpf;

    @NotBlank
    private String cep;

    @NotBlank
    private String rua;

    @NotBlank
    private Integer numero;

    @NotBlank
    private String complemento;

    @NotBlank
    private String cidade;

    @NotBlank
    private String uf;

}