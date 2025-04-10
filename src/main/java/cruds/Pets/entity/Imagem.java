package cruds.Pets.entity;

import jakarta.persistence.Lob;
import jakarta.persistence.Embeddable;

@Embeddable
public class Imagem {

    @Lob
    private byte[] dados;

    public Imagem() {}

    public Imagem(byte[] dados) {
        this.dados = dados;
    }

    public byte[] getDados() {
        return dados;
    }

    public void setDados(byte[] dados) {
        this.dados = dados;
    }
}