package cruds.Ong.entity;

import cruds.Imagem.entity.ImagemForms;
import cruds.Imagem.entity.ImagemOng;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "ong")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Ong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "nome")
    private String nome;

    @Column(name = "razaoSocial")
    private String razaoSocial;

    @Column(name = "senha")
    private String senha;

    @Column(name = "email")
    private String email;

    @OneToOne
    private ImagemOng imagemOng;

    @Column(name = "link")
    private String link;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public ImagemOng getImagemOng() {
        return imagemOng;
    }

    public void setImagemOng(ImagemOng imagemOng) {
        this.imagemOng = imagemOng;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
