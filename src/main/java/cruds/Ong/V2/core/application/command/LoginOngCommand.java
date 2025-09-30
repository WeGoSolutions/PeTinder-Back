package cruds.Ong.V2.core.application.command;

public class LoginOngCommand {
    
    private final String email;
    private final String senha;
    
    public LoginOngCommand(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }
    
    // Getters
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
}
