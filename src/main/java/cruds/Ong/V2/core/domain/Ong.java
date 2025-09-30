package cruds.Ong.V2.core.domain;

import java.util.UUID;

public class Ong {
    
    private UUID id;
    private String cnpj;
    private String cpf;
    private String nome;
    private String razaoSocial;
    private String senha;
    private String email;
    private String link;
    private Endereco endereco;
    private ImagemOng imagemOng;
    
    // Construtor principal
    public Ong(UUID id, String cnpj, String cpf, String nome, String razaoSocial,
               String senha, String email, String link) {
        this.id = id;
        this.cnpj = cnpj;
        this.cpf = cpf;
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.senha = senha;
        this.email = email;
        this.link = link;
        validarDados();
    }
    
    // Construtor para criação (sem ID)
    public Ong(String cnpj, String cpf, String nome, String razaoSocial,
               String senha, String email, String link) {
        this(null, cnpj, cpf, nome, razaoSocial, senha, email, link);
    }
    
    private void validarDados() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (nome.length() < 3) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        validarEmail();
        if (razaoSocial == null || razaoSocial.trim().isEmpty()) {
            throw new IllegalArgumentException("Razão social é obrigatória");
        }
        validarDocumento();
    }
    
    private void validarEmail() {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Email deve ter formato válido");
        }
    }
    
    private void validarDocumento() {
        // Deve ter CNPJ ou CPF
        if ((cnpj == null || cnpj.trim().isEmpty()) && (cpf == null || cpf.trim().isEmpty())) {
            throw new IllegalArgumentException("CNPJ ou CPF é obrigatório");
        }
        
        // Validar formato se fornecido
        if (cnpj != null && !cnpj.trim().isEmpty() && !cnpj.matches("\\d{14}")) {
            throw new IllegalArgumentException("CNPJ deve conter 14 dígitos");
        }
        if (cpf != null && !cpf.trim().isEmpty() && !cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos");
        }
    }
    
    // Getters
    public UUID getId() { return id; }
    public String getCnpj() { return cnpj; }
    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getRazaoSocial() { return razaoSocial; }
    public String getSenha() { return senha; }
    public String getEmail() { return email; }
    public String getLink() { return link; }
    public Endereco getEndereco() { return endereco; }
    public ImagemOng getImagemOng() { return imagemOng; }
    
    // Setters necessários para persistência e atualização
    public void setId(UUID id) { this.id = id; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
    public void setImagemOng(ImagemOng imagemOng) { this.imagemOng = imagemOng; }
    
    // Métodos de negócio
    public void atualizarSenha(String novaSenha) {
        this.senha = novaSenha;
    }
    
    public void atualizarEndereco(Endereco novoEndereco) {
        this.endereco = novoEndereco;
    }
    
    public void atualizarImagemOng(ImagemOng novaImagem) {
        this.imagemOng = novaImagem;
    }
    
    public Ong comNovoId(UUID novoId) {
        Ong ong = new Ong(novoId, cnpj, cpf, nome, razaoSocial, senha, email, link);
        ong.setEndereco(endereco);
        ong.setImagemOng(imagemOng);
        return ong;
    }
    
    public Ong comNovaImagem(ImagemOng novaImagem) {
        Ong ong = new Ong(id, cnpj, cpf, nome, razaoSocial, senha, email, link);
        ong.setEndereco(endereco);
        ong.setImagemOng(novaImagem);
        return ong;
    }
    
    public Ong comNovoEndereco(Endereco novoEndereco) {
        Ong ong = new Ong(id, cnpj, cpf, nome, razaoSocial, senha, email, link);
        ong.setEndereco(novoEndereco);
        ong.setImagemOng(imagemOng);
        return ong;
    }
}
