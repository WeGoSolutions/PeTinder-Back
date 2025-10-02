package cruds.Ong.V2.core.adapter;

import java.util.UUID;

public interface ArmazenamentoImagemOngGateway {

    String salvarImagem(byte[] dados, UUID ongId) throws Exception;

    byte[] buscarImagem(String caminho) throws Exception;

    void removerImagem(String caminho) throws Exception;
}
