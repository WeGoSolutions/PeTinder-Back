package cruds.Users.controller;


import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import cruds.Users.controller.dto.request.UserRequestListarDto;
import cruds.Users.controller.dto.request.UserRequestLoginDTO;
import cruds.Users.controller.dto.request.UserRequestTokenDto;
import cruds.Users.entity.User;

public class UsuarioMapper {

  public static User of(UserRequestCriarDTO usuarioCriacaoDto) {
    User usuario = new User();

    usuario.setEmail(usuarioCriacaoDto.getEmail());
    usuario.setNome(usuarioCriacaoDto.getNome());
    usuario.setSenha(usuarioCriacaoDto.getSenha());

    return usuario;
  }

  public static User of(UserRequestLoginDTO usuarioLoginDto) {
    User usuario = new User();

    usuario.setEmail(usuarioLoginDto.getEmail());
    usuario.setSenha(usuarioLoginDto.getSenha());

    return usuario;
  }

  public static UserRequestTokenDto of(User usuario, String token) {
    UserRequestTokenDto usuarioTokenDto = new UserRequestTokenDto();

    usuarioTokenDto.setUserId(usuario.getId());
    usuarioTokenDto.setEmail(usuario.getEmail());
    usuarioTokenDto.setNome(usuario.getNome());
    usuarioTokenDto.setToken(token);

    return usuarioTokenDto;
  }

  public static UserRequestListarDto of(User usuario) {
    UserRequestListarDto usuarioListarDto = new UserRequestListarDto();

    usuarioListarDto.setId(usuario.getId());
    usuarioListarDto.setEmail(usuario.getEmail());
    usuarioListarDto.setNome(usuario.getNome());

    return usuarioListarDto;
  }
}
