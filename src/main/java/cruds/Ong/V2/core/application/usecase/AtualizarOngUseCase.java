package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.command.AtualizarOngCommand;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.Ong;

public class AtualizarOngUseCase {
    
    private final OngGateway ongGateway;

    public AtualizarOngUseCase(OngGateway ongGateway) {
        this.ongGateway = ongGateway;
    }

    public Ong atualizar(AtualizarOngCommand command) {
        // Buscar ONG existente
        Ong ongExistente = ongGateway.buscarPorId(command.getId())
            .orElseThrow(() -> new OngException.OngNaoEncontradaException(
                "ONG com ID " + command.getId() + " não encontrada"
            ));

        // Validar se email já existe (se mudou)
        if (!ongExistente.getEmail().equals(command.getEmail()) && 
            ongGateway.emailJaExiste(command.getEmail())) {
            throw new OngException.EmailJaExisteException(
                "Email já está em uso: " + command.getEmail()
            );
        }

        // Criar nova instância de Ong com dados atualizados
        Ong ongAtualizada = new Ong(
            command.getId(),
            command.getCnpj(),
            command.getCpf(),
            command.getNome(),
            command.getRazaoSocial(),
            ongExistente.getSenha(), // Mantém senha existente
            command.getEmail(),
            command.getLink()
        );

        // Atualizar endereço se fornecido
        if (command.getEndereco() != null) {
            ongAtualizada.setEndereco(command.getEndereco());
        } else {
            ongAtualizada.setEndereco(ongExistente.getEndereco());
        }

        // Mantém imagem existente
        ongAtualizada.setImagemOng(ongExistente.getImagemOng());

        // Atualizar ONG
        return ongGateway.atualizar(ongAtualizada);
    }
}
