package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.exception.UsuarioException;

import java.util.UUID;

public class RemoverUsuarioUseCase {
    
    private final UsuarioGateway usuarioGateway;

    public RemoverUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public void apagarUser(UUID usuarioId) {
        if (!usuarioGateway.buscarPorId(usuarioId).isPresent()) {
            throw new UsuarioException.UsuarioNaoEncontradoException(
                "Usuário não encontrado: " + usuarioId
            );
        }

        usuarioGateway.remover(usuarioId);
    }
}
