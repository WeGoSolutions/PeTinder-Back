package cruds.common.strategy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalImageStorageStrategy implements ImageStorageStrategy {
    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/Desktop/S3 local/imagens/";

    @Override
    public void salvarImagem(byte[] imagemBytes, String nomeArquivo) throws IOException {
        Path path = Paths.get(UPLOAD_DIR + nomeArquivo);
        Files.createDirectories(path.getParent());
        Files.write(path, imagemBytes);
    }

    @Override
    public String gerarCaminho(String nomeArquivo) {
        return UPLOAD_DIR + nomeArquivo;
    }

}
