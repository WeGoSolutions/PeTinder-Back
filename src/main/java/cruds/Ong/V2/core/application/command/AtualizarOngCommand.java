package cruds.Ong.V2.core.application.command;

import cruds.Ong.V2.core.domain.Endereco;
import java.util.UUID;

public class AtualizarOngCommand {
    
    private final UUID id;
    private final String cnpj;
    private final String cpf;
    private final String nome;
    private final String razaoSocial;
    private final String email;
    private final String link;
    private final Endereco endereco;
    
    public AtualizarOngCommand(UUID id, String cnpj, String cpf, String nome,
                              String razaoSocial, String email, String link, Endereco endereco) {
        this.id = id;
        this.cnpj = cnpj;
        this.cpf = cpf;
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.email = email;
        this.link = link;
        this.endereco = endereco;
    }
    
    // Getters
    public UUID getId() { return id; }
    public String getCnpj() { return cnpj; }
    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getRazaoSocial() { return razaoSocial; }
    public String getEmail() { return email; }
    public String getLink() { return link; }
    public Endereco getEndereco() { return endereco; }
}
