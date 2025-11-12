//package cruds.Users.controller.dto.request;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Pattern;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class UserRequestOptionalDTO {
//
//    @Pattern(regexp = "\\d{11}")
//    @Schema(description = "CPF do usuário", example = "12345678901")
//    private String cpf;
//
//    @Pattern(regexp = "\\d{8}")
//    @Schema(description = "CEP do usuário", example = "12345678")
//    private String cep;
//
//    @NotBlank
//    @Pattern(regexp = "^[\\p{L}\\s0-9]+$")
//    @Schema(description = "Rua do usuário", example = "Rua das Flores")
//    private String rua;
//
//    @NotNull
//    @Schema(description = "Número da residência do usuário", example = "123")
//    private Integer numero;
//
//    @Pattern(regexp = "^[\\p{L}\\s0-9]+$")
//    @Schema(description = "Complemento da residência do usuário", example = "Apto 101")
//    private String complemento;
//
//    @NotBlank
//    @Pattern(regexp = "^[\\p{L}\\s0-9]+$")
//    @Schema(description = "Cidade do usuário", example = "São Paulo")
//    private String cidade;
//
//    @NotBlank
//    @Pattern(regexp = "^[\\p{L}\\s0-9]+$")
//    @Schema(description = "Estado do usuário", example = "SP")
//    private String uf;
//}
