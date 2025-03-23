package cruds.Users.DTOs;

import jakarta.validation.constraints.*;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class UserDTO {

    @NotBlank
    @Size(min = 3)
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$")
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

    @Pattern(regexp = "^[0-9]{11}$")
    private String cpf;

    private String cep;

    private String rua;

    private Integer numero;

    private String cidade;

    private String uf;

    @AssertTrue(message = "A pessoa deve ter mais de 21 anos")
    public boolean isMaiorDe21() {
        if (dataNasc == null) {
            return false; // Garantir que a data de nascimento não seja nula
        }

        LocalDate dataNascimento = new java.sql.Date(dataNasc.getTime()).toLocalDate();
        LocalDate hoje = LocalDate.now();
        Period periodo = Period.between(dataNascimento, hoje);

        return periodo.getYears() >= 21; // Verifica se a idade é 21 ou mais
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
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

}
