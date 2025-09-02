package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.exception.UsuarioException;
import cruds.Users.V2.core.domain.Usuario;

public class BuscarUsuarioPorIdUseCase {
    
    private final UsuarioGateway usuarioGateway;

    public BuscarUsuarioPorIdUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario buscar(Long usuarioId) {
        return usuarioGateway.buscarPorId(usuarioId)
            .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException(
                "Usuário não encontrado: " + usuarioId
            ));
    }
}
