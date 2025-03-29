package cruds.Pets.DTOs;

import cruds.Pets.Tables.Pet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public class PetRequest {

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

    @PositiveOrZero
    private Integer curtidas;

    @NotEmpty
    private List<String> tags;

    @NotBlank
    private List<String> imagemBase64;

    public PetRequest() {}

    public PetRequest(String nome, Double idade, Double peso, Double altura, Integer curtidas, List<String> tags, List<String> imagemBase64) {
        this.nome = nome;
        this.idade = idade;
        this.peso = peso;
        this.altura = altura;
        this.curtidas = curtidas;
        this.tags = tags;
        this.imagemBase64 = imagemBase64;
    }

    public @NotBlank String getNome() {
        return nome;
    }

    public void setNome(@NotBlank String nome) {
        this.nome = nome;
    }

    public @Positive Double getIdade() {
        return idade;
    }

    public void setIdade(@Positive Double idade) {
        this.idade = idade;
    }

    public @NotBlank @Positive Double getPeso() {
        return peso;
    }

    public void setPeso(@NotBlank @Positive Double peso) {
        this.peso = peso;
    }

    public @NotBlank @Positive Double getAltura() {
        return altura;
    }

    public void setAltura(@NotBlank @Positive Double altura) {
        this.altura = altura;
    }

    public @PositiveOrZero Integer getCurtidas() {
        return curtidas;
    }

    public void setCurtidas(@NotBlank @Positive Integer curtidas) {
        this.curtidas = curtidas;
    }

    public @NotEmpty List<String> getTags() {
        return tags;
    }

    public void setTags(@NotEmpty List<String> tags) {
        this.tags = tags;
    }

    public @NotBlank List<String> getImagemBase64() {
        return imagemBase64;
    }

    public void setImagemBase64(@NotBlank List<String> imagemBase64) {
        this.imagemBase64 = imagemBase64;
    }

    public static Pet toEntity(PetRequest petRequest) {
        Pet pet = new Pet();
        pet.setNome(petRequest.getNome());
        pet.setIdade(petRequest.getIdade());
        pet.setPeso(petRequest.getPeso());
        pet.setAltura(petRequest.getAltura());
        pet.setCurtidas(petRequest.getCurtidas());
        pet.setTags(petRequest.getTags());
        return pet;
    }

}
