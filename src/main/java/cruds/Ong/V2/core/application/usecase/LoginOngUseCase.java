package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.CriptografiaGateway;
import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.command.LoginOngCommand;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.Ong;

public class LoginOngUseCase {
    
    private final OngGateway ongGateway;
    private final CriptografiaGateway criptografiaGateway;

    public LoginOngUseCase(OngGateway ongGateway, CriptografiaGateway criptografiaGateway) {
        this.ongGateway = ongGateway;
        this.criptografiaGateway = criptografiaGateway;
    }

    public Ong autenticar(LoginOngCommand command) {
        // Buscar ONG por email
        Ong ong = ongGateway.buscarPorEmail(command.getEmail())
            .orElseThrow(() -> new OngException.CredenciaisInvalidasException(
                "Email ou senha inválidos"
            ));

        // Validar senha
        if (!criptografiaGateway.verificarSenha(command.getSenha(), ong.getSenha())) {
            throw new OngException.CredenciaisInvalidasException("Email ou senha inválidos");
        }

        return ong;
    }
}
