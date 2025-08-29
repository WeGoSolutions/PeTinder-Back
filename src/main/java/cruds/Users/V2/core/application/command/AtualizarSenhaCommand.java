package cruds.Users.V2.core.application.command;

public class AtualizarSenhaCommand {
    
    private final Long usuarioId;
    private final String senhaAtual;
    private final String novaSenha;

    public AtualizarSenhaCommand(Long usuarioId, String senhaAtual, String novaSenha) {
        this.usuarioId = usuarioId;
        this.senhaAtual = senhaAtual;
        this.novaSenha = novaSenha;
    }

    public Long getUsuarioId() { return usuarioId; }
    public String getSenhaAtual() { return senhaAtual; }
    public String getNovaSenha() { return novaSenha; }
}
