package cruds.Users.V2.core.adapter;

public interface AutenticacaoGateway {

    boolean autenticar(String email, String senha);

    String autenticarEGerarToken(String email, String Senha);

    boolean validarToken(String token);

    String extrairEmailDoToken(String token);
}
