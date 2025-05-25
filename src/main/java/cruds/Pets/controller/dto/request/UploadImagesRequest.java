package cruds.Pets.controller.dto.request;

import java.util.List;

public class UploadImagesRequest {
    private List<byte[]> imagensBytes;
    private List<String> nomesArquivos;

    public List<byte[]> getImagensBytes() {
        return imagensBytes;
    }

    public void setImagensBytes(List<byte[]> imagensBytes) {
        this.imagensBytes = imagensBytes;
    }

    public List<String> getNomesArquivos() {
        return nomesArquivos;
    }

    public void setNomesArquivos(List<String> nomesArquivos) {
        this.nomesArquivos = nomesArquivos;
    }
}