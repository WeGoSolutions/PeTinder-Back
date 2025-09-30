package cruds.Ong.V2.core.adapter;

public interface ArmazenamentoImagemGateway {
    
    String salvarImagem(byte[] dados, String caminhoCompleto);
    
    byte[] recuperarImagem(String caminhoCompleto);
    
    void removerImagem(String caminhoCompleto);
}
