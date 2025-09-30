package cruds.Ong.V2.core.application.command;

import java.util.UUID;

public class AtualizarSenhaOngCommand {
    
    private final UUID id;
    private final String senhaAtual;
    private final String novaSenha;
    
    public AtualizarSenhaOngCommand(UUID id, String senhaAtual, String novaSenha) {
        this.id = id;
        this.senhaAtual = senhaAtual;
        this.novaSenha = novaSenha;
    }
    
    // Getters
    public UUID getId() { return id; }
    public String getSenhaAtual() { return senhaAtual; }
    public String getNovaSenha() { return novaSenha; }
}
