package sptech.school.crud_imagem.DTOs;

public class PetDTO {

    private String nome;
    private Integer idade;
    private Double peso;
    private Double altura;
    private String imagemBase64;

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Integer getIdade() {
        return idade;
    }
    public void setIdade(Integer idade) {
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
    public String getImagemBase64() {
        return imagemBase64;
    }
    public void setImagemBase64(String imagemBase64) {
        this.imagemBase64 = imagemBase64;
    }
}