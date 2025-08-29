package cruds.Users.V2.core.domain;

import java.util.Arrays;

/**
 * Value Object ImagemUsuario - Clean Architecture
 * Representa a imagem do usuário
 */
public class ImagemUsuario {
    
    private final Integer id;
    private final byte[] dados;
    private final String nomeArquivo;

    public ImagemUsuario(Integer id, byte[] dados, String nomeArquivo) {
        this.id = id;
        this.dados = dados != null ? Arrays.copyOf(dados, dados.length) : null;
        this.nomeArquivo = nomeArquivo;
        validarImagem();
    }

    // Construtor para criação (sem ID)
    public ImagemUsuario(byte[] dados, String nomeArquivo) {
        this(null, dados, nomeArquivo);
    }

    private void validarImagem() {
        if (dados != null && dados.length == 0) {
            throw new IllegalArgumentException("Dados da imagem não podem estar vazios");
        }
        
        if (dados != null && dados.length > 10 * 1024 * 1024) { // 10MB
            throw new IllegalArgumentException("Imagem não pode ser maior que 10MB");
        }
    }

    // Getters
    public Integer getId() { 
        return id; 
    }
    
    public byte[] getDados() { 
        return dados != null ? Arrays.copyOf(dados, dados.length) : null; 
    }
    
    public String getNomeArquivo() { 
        return nomeArquivo; 
    }

    public boolean temImagem() {
        return dados != null && dados.length > 0;
    }

    // Value Objects são imutáveis, então criamos um novo para "atualizar"
    public ImagemUsuario comNovoId(Integer novoId) {
        return new ImagemUsuario(novoId, dados, nomeArquivo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ImagemUsuario that = (ImagemUsuario) obj;
        
        return Arrays.equals(dados, that.dados) &&
               nomeArquivo != null ? nomeArquivo.equals(that.nomeArquivo) : that.nomeArquivo == null;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(dados);
        result = 31 * result + (nomeArquivo != null ? nomeArquivo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ImagemUsuario{" +
                "id=" + id +
                ", nomeArquivo='" + nomeArquivo + '\'' +
                ", tamanho=" + (dados != null ? dados.length : 0) + " bytes" +
                '}';
    }
}
