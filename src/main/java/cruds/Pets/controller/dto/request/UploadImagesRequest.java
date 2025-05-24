package cruds.Pets.controller.dto.request;

import java.util.List;

public class UploadImagesRequest {
    private List<String> imagensBase64;
    private List<String> nomesArquivos;

    public List<String> getImagensBase64() {
        return imagensBase64;
    }

    public void setImagensBase64(List<String> imagensBase64) {
        this.imagensBase64 = imagensBase64;
    }

    public List<String> getNomesArquivos() {
        return nomesArquivos;
    }

    public void setNomesArquivos(List<String> nomesArquivos) {
        this.nomesArquivos = nomesArquivos;
    }
}