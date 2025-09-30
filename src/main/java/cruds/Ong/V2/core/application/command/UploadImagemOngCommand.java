package cruds.Ong.V2.core.application.command;

import java.util.UUID;

public class UploadImagemOngCommand {
    
    private final UUID ongId;
    private final byte[] dados;
    private final String nomeArquivo;
    
    public UploadImagemOngCommand(UUID ongId, byte[] dados, String nomeArquivo) {
        this.ongId = ongId;
        this.dados = dados;
        this.nomeArquivo = nomeArquivo;
    }
    
    // Getters
    public UUID getOngId() { return ongId; }
    public byte[] getDados() { return dados; }
    public String getNomeArquivo() { return nomeArquivo; }
}
