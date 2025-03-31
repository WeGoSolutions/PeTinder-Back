package cruds.Users.DTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserOptional {

    @Pattern(regexp = "\\d{11}")
    private String cpf;

    @Pattern(regexp = "\\d{8}")
    private String cep;

    @NotBlank
    private String rua;

    @NotBlank
    private Integer numero;

    private String complemento;

    @NotBlank
    private String cidade;

    @NotBlank
    private String uf;

    public UserOptional() {}

    public UserOptional(String cpf, String cep, String rua, Integer numero, String complemento, String cidade, String uf) {
        this.cpf = cpf;
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.cidade = cidade;
        this.uf = uf;
    }

    public @Pattern(regexp = "\\d{11}") String getCpf() {
        return cpf;
    }

    public void setCpf(@Pattern(regexp = "\\d{11}") String cpf) {
        this.cpf = cpf;
    }

    public @Pattern(regexp = "\\d{8}") String getCep() {
        return cep;
    }

    public void setCep(@Pattern(regexp = "\\d{8}") String cep) {
        this.cep = cep;
    }

    public @NotBlank String getRua() {
        return rua;
    }

    public void setRua(@NotBlank String rua) {
        this.rua = rua;
    }

    public @NotBlank Integer getNumero() {
        return numero;
    }

    public void setNumero(@NotBlank Integer numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public @NotBlank String getCidade() {
        return cidade;
    }

    public void setCidade(@NotBlank String cidade) {
        this.cidade = cidade;
    }

    public @NotBlank String getUf() {
        return uf;
    }

    public void setUf(@NotBlank String uf) {
        this.uf = uf;
    }

    public static UserOptional toOptional(@Valid UserRequest user) {
        UserOptional dto = new UserOptional();
        dto.setCpf(user.getCpf());
        dto.setCep(user.getCep());
        dto.setRua(user.getRua());
        dto.setNumero(user.getNumero());
        dto.setComplemento(user.getComplemento());
        dto.setCidade(user.getCidade());
        dto.setUf(user.getUf());
        return dto;
    }

}
