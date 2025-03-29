package cruds.Users.DTOs;

import cruds.Users.Tables.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class UserRequest {

    @NotBlank
    @Size(min = 3)
    @Pattern(regexp = "^[A-Za-zÀ-Ö ]+$")
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z0-9!@#$%^&*(),.?\":{}|<>]+$")
    private String senha;

    @Past
    private Date dataNasc;

//    elas podem estar vazias se forem preenchidas depois utiliza UserOptional
//    é no UserOptional em que as validações são feitas
    private String cpf;
    private String cep;
    private String rua;
    private Integer numero;
    private String complemento;
    private String cidade;
    private String uf;

    @AssertTrue(message = "A pessoa deve ter mais de 21 anos")
    public boolean isMaiorDe21() {
        if (dataNasc == null) {
            return false;
        }

        LocalDate dataNascimento = new java.sql.Date(dataNasc.getTime()).toLocalDate();
        LocalDate hoje = LocalDate.now();
        Period periodo = Period.between(dataNascimento, hoje);

        return periodo.getYears() >= 21;
    }

    public UserRequest() {}

    public UserRequest(String nome, String email, String senha, Date dataNasc, String cpf, String cep, String rua, Integer numero, String complemento, String cidade, String uf) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNasc = dataNasc;
        this.cpf = cpf;
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.cidade = cidade;
        this.uf = uf;
    }

    public @NotBlank @Size(min = 3) @Pattern(regexp = "^[A-Za-zÀ-Ö ]+$") String getNome() {
        return nome;
    }

    public void setNome(@NotBlank @Size(min = 3) @Pattern(regexp = "^[A-Za-zÀ-Ö ]+$") String nome) {
        this.nome = nome;
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank @Size(min = 6) @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z0-9!@#$%^&*(),.?\":{}|<>]+$") String getSenha() {
        return senha;
    }

    public void setSenha(@NotBlank @Size(min = 6) @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z0-9!@#$%^&*(),.?\":{}|<>]+$") String senha) {
        this.senha = senha;
    }

    public @Past Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(@Past Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public static User toEntity(@Valid UserRequest usuario){
        User dto = new User();
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setSenha(usuario.getSenha());
        dto.setDataNasc(usuario.getDataNasc());
        dto.setCpf(usuario.getCpf());
        dto.setCep(usuario.getCep());
        dto.setRua(usuario.getRua());
        dto.setNumero(usuario.getNumero());
        dto.setComplemento(usuario.getComplemento());
        dto.setCidade(usuario.getCidade());
        dto.setUf(usuario.getUf());
        return dto;
    }

}
