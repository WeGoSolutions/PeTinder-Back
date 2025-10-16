package cruds.Pets.V2.core.domain;

import java.util.UUID;

public class ImagemPet {
    
    private UUID id;

    private String nomeArquivo;
    private byte[] dados;
    
    public ImagemPet(UUID id, String nomeArquivo, byte[] dados) {
        this.id = id;
        this.nomeArquivo = nomeArquivo;
        this.dados = dados;
    }
    
    public ImagemPet(String nomeArquivo, byte[] dados) {
        this(null, nomeArquivo, dados);
    }
    

    public boolean temDados() {
        return dados != null && dados.length > 0;
    }
    
    // Getters
    public UUID getId() { return id; }
    public String getNomeArquivo() { return nomeArquivo; }
    public byte[] getDados() { return dados; }
    
    // Setters necessários para persistência
    public void setId(UUID id) { this.id = id; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }
    public void setDados(byte[] dados) { this.dados = dados; }
}