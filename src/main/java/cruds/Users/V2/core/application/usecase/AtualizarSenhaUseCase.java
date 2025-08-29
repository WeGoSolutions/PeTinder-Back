package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.CriptografiaGateway;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.command.AtualizarSenhaCommand;
import cruds.Users.V2.core.application.exception.UsuarioException;
import cruds.Users.V2.core.domain.Usuario;

public class AtualizarSenhaUseCase {
    
    private final UsuarioGateway usuarioGateway;
    private final CriptografiaGateway criptografiaGateway;

    public AtualizarSenhaUseCase(UsuarioGateway usuarioGateway, CriptografiaGateway criptografiaGateway) {
        this.usuarioGateway = usuarioGateway;
        this.criptografiaGateway = criptografiaGateway;
    }

    public Usuario executar(AtualizarSenhaCommand command) {
        Usuario usuario = usuarioGateway.buscarPorId(command.getUsuarioId())
            .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException(
                "Usuário não encontrado: " + command.getUsuarioId()
            ));

        if (!criptografiaGateway.verificarSenha(command.getSenhaAtual(), usuario.getSenha())) {
            throw new UsuarioException.SenhaInvalidaException("Senha atual incorreta");
        }

        String novaSenhaCriptografada = criptografiaGateway.criptografarSenha(command.getNovaSenha());
        usuario.atualizarSenha(novaSenhaCriptografada);

        return usuarioGateway.atualizar(usuario);
    }
}
