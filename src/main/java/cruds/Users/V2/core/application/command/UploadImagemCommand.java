package cruds.Users.V2.core.application.command;

import cruds.Users.V2.core.domain.ImagemUsuario;

public class UploadImagemCommand {
    
    private final Long usuarioId;
    private final byte[] dadosImagem;
    private final String nomeArquivo;

    public UploadImagemCommand(Long usuarioId, byte[] dadosImagem, String nomeArquivo) {
        this.usuarioId = usuarioId;
        this.dadosImagem = dadosImagem;
        this.nomeArquivo = nomeArquivo;
    }

    public Long getUsuarioId() { return usuarioId; }
    
    public ImagemUsuario criarImagemUsuario() {
        return new ImagemUsuario(dadosImagem, nomeArquivo);
    }
}
