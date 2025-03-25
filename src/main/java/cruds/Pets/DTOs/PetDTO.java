package cruds.Pets.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public class PetDTO {

    @NotBlank
    private String nome;

    @Positive
    private Double idade;

    @NotBlank
    @Positive
    private Double peso;

    @NotBlank
    @Positive
    private Double altura;

    @NotBlank
    @Positive
    private Integer curtidas;

    private List<String> tags;

    @NotBlank
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