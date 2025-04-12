package cruds.common.util;

import org.apache.commons.io.FilenameUtils;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

public class ImageValidationUtil {

    private static final String[] TIPOS_PERMITIDOS = {"jpg", "jpeg", "png"};
    private static final long TAMANHO_MAXIMO = 5 * 1024 * 1024; // 5MB

    private static void validateCommon(byte[] imagem, String nomeArquivo) throws IOException {
        String tipoArquivo = FilenameUtils.getExtension(nomeArquivo);
        if (!Arrays.asList(TIPOS_PERMITIDOS).contains(tipoArquivo.toLowerCase())) {
            throw new IllegalArgumentException("Tipo de arquivo não permitido. Apenas JPG e PNG são aceitos.");
        }
        if (imagem.length > TAMANHO_MAXIMO) {
            throw new IllegalArgumentException("Tamanho da imagem excede o limite de 5MB.");
        }
        if (ImageIO.read(new ByteArrayInputStream(imagem)) == null) {
            throw new IllegalArgumentException("Arquivo enviado não é uma imagem válida.");
        }
    }

    public static void validateUserImage(byte[] imagem, String nomeArquivo) throws IOException {
        if (imagem == null || imagem.length == 0) {
            throw new IllegalArgumentException("Imagem de perfil é obrigatória.");
        }
        validateCommon(imagem, nomeArquivo);
    }

    public static void validatePetImages(List<byte[]> imagens, List<String> nomesArquivos) throws IOException {
        if (imagens == null || imagens.isEmpty()) {
            throw new IllegalArgumentException("Ao menos uma imagem é obrigatória para o pet.");
        }
        if (imagens.size() > 5) {
            throw new IllegalArgumentException("Máximo de 5 imagens permitidas para o pet.");
        }
        for (int i = 0; i < imagens.size(); i++) {
            validateCommon(imagens.get(i), nomesArquivos.get(i));
        }
    }

    public static void validateFormImages(List<byte[]> imagens, List<String> nomesArquivos) throws IOException {
        if (imagens == null || imagens.size() < 5) {
            throw new IllegalArgumentException("Ao menos 5 imagens são obrigatórias no formulário.");
        }
        for (int i = 0; i < imagens.size(); i++) {
            validateCommon(imagens.get(i), nomesArquivos.get(i));
        }
    }
}