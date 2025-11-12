//package cruds.Ong.controller.dto.request;
//
//import cruds.Ong.entity.Ong;
//import cruds.Users.controller.dto.request.EnderecoRequestDTO;
//import cruds.Users.entity.Endereco;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Size;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class OngRequestCriarDTO {
//
//    @Pattern(regexp = "\\d{14}")
//    private String cnpj;
//
//    @Pattern(regexp = "\\d{11}")
//    private String cpf;
//
//    @NotBlank
//    @Size(min = 3)
//    @Pattern(regexp = "^[A-Za-zÀ-Ö ]+$")
//    private String nome;
//
//    @NotBlank
//    private String razaoSocial;
//
//    @NotBlank
//    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\\\":{}|<>]+$")
//    private String senha;
//
//    @NotBlank
//    @Email
//    private String email;
//
//    @NotBlank
//    private String link;
//
//    private EnderecoRequestDTO endereco;
//
//    public static Ong toEntity(OngRequestCriarDTO ongRequest) {
//        Endereco endereco = null;
//        if (ongRequest.getEndereco() != null) {
//            endereco = Endereco.builder()
//                    .cep(ongRequest.getEndereco().getCep())
//                    .rua(ongRequest.getEndereco().getRua())
//                    .numero(ongRequest.getEndereco().getNumero())
//                    .cidade(ongRequest.getEndereco().getCidade())
//                    .uf(ongRequest.getEndereco().getUf())
//                    .complemento(ongRequest.getEndereco().getComplemento())
//                    .build();
//        }
//
//        return Ong.builder()
//                .cnpj(ongRequest.getCnpj())
//                .cpf(ongRequest.getCpf())
//                .nome(ongRequest.getNome())
//                .razaoSocial(ongRequest.getRazaoSocial())
//                .senha(ongRequest.getSenha())
//                .email(ongRequest.getEmail())
//                .link(ongRequest.getLink())
//                .endereco(endereco)
//                .build();
//    }
//}
