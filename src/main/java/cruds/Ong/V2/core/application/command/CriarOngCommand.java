package cruds.Ong.V2.core.application.command;

import cruds.Ong.V2.core.domain.Endereco;

public class CriarOngCommand {
    
    private final String cnpj;
    private final String cpf;
    private final String nome;
    private final String razaoSocial;
    private final String senha;
    private final String email;
    private final String link;
    private final Endereco endereco;
    
    public CriarOngCommand(String cnpj, String cpf, String nome, String razaoSocial,
                          String senha, String email, String link, Endereco endereco) {
        this.cnpj = cnpj;
        this.cpf = cpf;
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.senha = senha;
        this.email = email;
        this.link = link;
        this.endereco = endereco;
    }
    
    // Getters
    public String getCnpj() { return cnpj; }
    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getRazaoSocial() { return razaoSocial; }
    public String getSenha() { return senha; }
    public String getEmail() { return email; }
    public String getLink() { return link; }
    public Endereco getEndereco() { return endereco; }
}
