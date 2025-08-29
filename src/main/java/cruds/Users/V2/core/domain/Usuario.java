package cruds.Users.V2.core.domain;

import java.time.LocalDate;
import java.time.Period;

public class Usuario {
    
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dataNascimento;
    private String cpf;
    private Endereco endereco;
    private ImagemUsuario imagemUsuario;
    private Boolean usuarioNovo;

    // Construtor
    public Usuario(Long id, String nome, String email, String senha, 
                   LocalDate dataNascimento, String cpf, Boolean usuarioNovo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.usuarioNovo = usuarioNovo != null ? usuarioNovo : true;
        validarIdade();
        validarEmail();
        validarNome();
    }

    // Construtor para criação (sem ID)
    public Usuario(String nome, String email, String senha, LocalDate dataNascimento) {
        this(null, nome, email, senha, dataNascimento, null, true);
    }

    // Regras de negócio - Validações
    private void validarIdade() {
        if (dataNascimento == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória");
        }
        
        LocalDate hoje = LocalDate.now();
        Period periodo = Period.between(dataNascimento, hoje);
        
        if (periodo.getYears() < 21) {
            throw new IllegalArgumentException("Usuário deve ter mais de 21 anos");
        }
    }

    private void validarEmail() {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Email deve ter formato válido");
        }
    }

    private void validarNome() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        
        if (nome.trim().length() < 3) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres");
        }
        
        String nomeRegex = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$";
        if (!nome.matches(nomeRegex)) {
            throw new IllegalArgumentException("Nome deve conter apenas letras e espaços");
        }
    }

    // Métodos de negócio
    public boolean isMaiorDe21() {
        if (dataNascimento == null) {
            return false;
        }
        LocalDate hoje = LocalDate.now();
        Period periodo = Period.between(dataNascimento, hoje);
        return periodo.getYears() >= 21;
    }

    public void atualizarSenha(String novaSenha) {
        validarSenha(novaSenha);
        this.senha = novaSenha;
    }

    public void marcarComoUsuarioExperiente() {
        this.usuarioNovo = false;
    }

    public void atualizarEndereco(Endereco novoEndereco) {
        this.endereco = novoEndereco;
    }

    public void atualizarImagemUsuario(ImagemUsuario novaImagem) {
        this.imagemUsuario = novaImagem;
    }

    public void atualizarInformacoesOpcionais(String cpf, Endereco endereco) {
        this.cpf = cpf;
        this.endereco = endereco;
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < 8) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 8 caracteres");
        }
        
        String senhaRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\\\":{}|<>]+$";
        if (!senha.matches(senhaRegex)) {
            throw new IllegalArgumentException("Senha deve conter pelo menos: uma letra maiúscula, uma minúscula, um número e um caractere especial");
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getCpf() { return cpf; }
    public Endereco getEndereco() { return endereco; }
    public ImagemUsuario getImagemUsuario() { return imagemUsuario; }
    public Boolean getUsuarioNovo() { return usuarioNovo; }

    // Setters para uso interno
    void setId(Long id) { this.id = id; }
    
    // Equals e HashCode baseados no email (business key)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return email != null ? email.equals(usuario.email) : usuario.email == null;
    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", usuarioNovo=" + usuarioNovo +
                '}';
    }
}
