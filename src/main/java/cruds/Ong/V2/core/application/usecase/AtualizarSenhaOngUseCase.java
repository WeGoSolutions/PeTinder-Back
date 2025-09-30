package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.CriptografiaGateway;
import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.command.AtualizarSenhaOngCommand;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.Ong;

public class AtualizarSenhaOngUseCase {
    
    private final OngGateway ongGateway;
    private final CriptografiaGateway criptografiaGateway;

    public AtualizarSenhaOngUseCase(OngGateway ongGateway, CriptografiaGateway criptografiaGateway) {
        this.ongGateway = ongGateway;
        this.criptografiaGateway = criptografiaGateway;
    }

    public Ong atualizar(AtualizarSenhaOngCommand command) {
        // Buscar ONG
        Ong ong = ongGateway.buscarPorId(command.getId())
            .orElseThrow(() -> new OngException.OngNaoEncontradaException(
                "ONG com ID " + command.getId() + " não encontrada"
            ));

        // Validar senha atual
        if (!criptografiaGateway.verificarSenha(command.getSenhaAtual(), ong.getSenha())) {
            throw new OngException.SenhaInvalidaException("Senha atual incorreta");
        }

        // Criptografar nova senha
        String novaSenhaCriptografada = criptografiaGateway.criptografarSenha(command.getNovaSenha());
        ong.atualizarSenha(novaSenhaCriptografada);

        // Atualizar ONG
        return ongGateway.atualizar(ong);
    }
}
