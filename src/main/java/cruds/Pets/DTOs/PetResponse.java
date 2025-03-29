package cruds.Pets.DTOs;

import cruds.Pets.Tables.Pet;

public class PetResponse {

    private String nome;
    private Double idade;
    private Integer curtidas;

    public PetResponse() {}

    public PetResponse(String nome, Double idade, Integer curtidas) {
        this.nome = nome;
        this.idade = idade;
        this.curtidas = curtidas;
    }

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

    public Integer getCurtidas() {
        return curtidas;
    }

    public void setCurtidas(Integer curtidas) {
        this.curtidas = curtidas;
    }

    public static PetResponse toResponse(Pet pet) {
        PetResponse dto = new PetResponse();
        dto.setNome(pet.getNome());
        dto.setIdade(pet.getIdade());
        dto.setCurtidas(pet.getCurtidas());
        return dto;
    }

}
