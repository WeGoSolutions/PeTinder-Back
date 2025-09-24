package cruds.Pets.V2.core.domain;

import java.util.UUID;

public class ImagemPet {
    
    private UUID id;
    private String caminho;
    private String nomeArquivo;
    private byte[] dados;
    
    public ImagemPet(UUID id, String caminho, String nomeArquivo, byte[] dados) {
        this.id = id;
        this.caminho = caminho;
        this.nomeArquivo = nomeArquivo;
        this.dados = dados;
    }
    
    public ImagemPet(String caminho, String nomeArquivo, byte[] dados) {
        this(null, caminho, nomeArquivo, dados);
    }
    
    public ImagemPet(String caminho) {
        this(null, caminho, null, null);
    }
    
    public boolean temDados() {
        return dados != null && dados.length > 0;
    }
    
    // Getters
    public UUID getId() { return id; }
    public String getCaminho() { return caminho; }
    public String getNomeArquivo() { return nomeArquivo; }
    public byte[] getDados() { return dados; }
    
    // Setters necessários para persistência
    public void setId(UUID id) { this.id = id; }
    public void setCaminho(String caminho) { this.caminho = caminho; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }
    public void setDados(byte[] dados) { this.dados = dados; }
}