package cruds.Ong.V2.core.adapter;

public interface CriptografiaGateway {
    
    String criptografarSenha(String senha);
    
    boolean verificarSenha(String senhaRaw, String senhaCriptografada);
}
