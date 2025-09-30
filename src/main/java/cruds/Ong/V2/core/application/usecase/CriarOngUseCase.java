package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.CriptografiaGateway;
import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.command.CriarOngCommand;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.Ong;

public class CriarOngUseCase {
    
    private final OngGateway ongGateway;
    private final CriptografiaGateway criptografiaGateway;

    public CriarOngUseCase(OngGateway ongGateway, CriptografiaGateway criptografiaGateway) {
        this.ongGateway = ongGateway;
        this.criptografiaGateway = criptografiaGateway;
    }

    public Ong cadastrar(CriarOngCommand command) {
        // Validar se email já existe
        if (ongGateway.emailJaExiste(command.getEmail())) {
            throw new OngException.EmailJaExisteException(
                "Email já está em uso: " + command.getEmail()
            );
        }

        // Criar domínio Ong
        Ong ong = new Ong(
            command.getCnpj(),
            command.getCpf(),
            command.getNome(),
            command.getRazaoSocial(),
            command.getSenha(),
            command.getEmail(),
            command.getLink()
        );

        // Criptografar senha
        String senhaCriptografada = criptografiaGateway.criptografarSenha(ong.getSenha());
        ong.atualizarSenha(senhaCriptografada);

        // Adicionar endereço se fornecido
        if (command.getEndereco() != null) {
            ong.setEndereco(command.getEndereco());
        }

        // Salvar ONG
        return ongGateway.salvar(ong);
    }
}
