package cruds.common.strategy;

import java.io.IOException;

public interface ImageStorageStrategy {
    void salvarImagem(byte[] imagemBytes, String nomeArquivo) throws IOException;
}
