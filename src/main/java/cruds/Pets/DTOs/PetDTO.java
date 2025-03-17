package cruds.Pets.DTOs;

import java.util.List;

public class PetDTO {

    private String nome;
    private Double idade;
    private Double peso;
    private Double altura;
    private Integer curtidas;
    private List<String> tags;
    private List<String> imagemBase64;

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Double getIdade() {
        return idade;
    }
    public void setIdade(Double idade) {
        this.idade = idade;
    }
    public Double getPeso() {
        return peso;
    }
    public void setPeso(Double peso) {
        this.peso = peso;
    }
    public Double getAltura() {
        return altura;
    }
    public void setAltura(Double altura) {
        this.altura = altura;
    }
    public Integer getCurtidas() {
        return curtidas;
    }
    public void setCurtidas(Integer curtidas) {
        this.curtidas = curtidas;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public List<String> getImagemBase64() {
        return imagemBase64;
    }
    public void setImagemBase64(List<String> imagemBase64) {
        this.imagemBase64 = imagemBase64;
    }
}