package cruds.Users.controller.dto.request;

import cruds.Users.entity.User;
import cruds.common.exception.NotAllowedException;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Nome do usuário", example = "Petinder")
    private String nome;

    @NotBlank
    @Email
    @Schema(description = "Email do usuário", example = "petinder@gmail.com")
    private String email;

    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\\\":{}|<>]+$")
    @Schema(description = "Senha do usuário", example = "Urubu@123")
    private String senha;

    @Past(message = "A data de nascimento deve ser no passado")
    @Schema(description = "Data de nascimento do usuário", example = "2000-01-01")
    private LocalDate dataNasc;

    @NotBlank
    private Boolean userNovo;

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
                .userNovo(true)
                    .build();
    }
}