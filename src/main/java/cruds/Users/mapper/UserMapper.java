package cruds.Users.mapper;

import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.controller.dto.response.UserResponseUrlDTO;
import cruds.Users.entity.Endereco;
import cruds.Users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  public User toEntity(UserRequestCriarDTO dto) {
    return User.builder()
            .nome(dto.getNome())
            .email(dto.getEmail())
            .dataNasc(dto.getDataNasc())
            .userNovo(true)
            .build();
  }

  public UserResponseCadastroDTO toCadastroResponse(User user) {
    UserResponseCadastroDTO.UserResponseCadastroDTOBuilder builder = UserResponseCadastroDTO.builder()
            .id(user.getId())
            .nome(user.getNome())
            .email(user.getEmail())
            .dataNasc(user.getDataNasc())
            .cpf(user.getCpf());

    if (user.getEndereco() != null) {
      Endereco endereco = user.getEndereco();
      builder.cep(endereco.getCep())
              .rua(endereco.getRua())
              .numero(endereco.getNumero())
              .cidade(endereco.getCidade())
              .uf(endereco.getUf())
              .complemento(endereco.getComplemento());
    }

    // A lógica de imagem seria tratada aqui, provavelmente para gerar a URL
    // builder.imageUrl( ... );

    return builder.build();
  }

  public UserResponseLoginDTO toLoginResponse(User user, String token) {
    return UserResponseLoginDTO.builder()
            .id(user.getId())
            .nome(user.getNome())
            .email(user.getEmail())
            .token(token)
            .userNovo(user.getUserNovo())
            .build();
  }

  public UserResponseUrlDTO toUrlResponse(User user, String imageUrl) {
    return UserResponseUrlDTO.builder()
            .id(user.getId())
            .nome(user.getNome())
            .email(user.getEmail())
            .imageUrl(imageUrl)
            .build();
  }
}