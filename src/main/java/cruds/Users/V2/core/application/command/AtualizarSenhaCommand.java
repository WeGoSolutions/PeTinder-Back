package cruds.Users.V2.core.application.command;

import java.util.UUID;

public class AtualizarSenhaCommand {
    
    private final UUID usuarioId;
    private final String senhaAtual;
    private final String novaSenha;

    public AtualizarSenhaCommand(UUID usuarioId, String senhaAtual, String novaSenha) {
        this.usuarioId = usuarioId;
        this.senhaAtual = senhaAtual;
        this.novaSenha = novaSenha;
    }

    public UUID getUsuarioId() { return usuarioId; }
    public String getSenhaAtual() { return senhaAtual; }
    public String getNovaSenha() { return novaSenha; }
}
