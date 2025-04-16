package cruds.Users.controller.dto.request;

import cruds.Users.entity.User;
import cruds.common.exception.NotAllowedException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
public class UserRequestCriarDTO {

    @NotBlank
    @Size(min = 3)
    @Pattern(regexp = "^[A-Za-zÀ-Ö ]+$")
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\\\":{}|<>]+$")
    private String senha;

    @Past(message = "A data de nascimento deve ser no passado")
    private LocalDate dataNasc;

    public boolean isMaiorDe21() {
        if (dataNasc == null) {
            return false;
        }
        LocalDate hoje = LocalDate.now();
        Period periodo = Period.between(dataNasc, hoje);
        if (periodo.getYears() >= 21) {
            return true;
        }
        throw new NotAllowedException("Usuário deve ter mais de 21 anos");
    }

    public static User toEntity(UserRequestCriarDTO dto) {
        return User.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .dataNasc(dto.getDataNasc())
                    .build();
    }
}