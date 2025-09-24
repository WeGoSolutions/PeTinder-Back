package cruds.Pets.V2.core.adapter;

import cruds.Pets.V2.core.domain.ImagemPet;

public interface ArmazenamentoImagemPetGateway {

    String salvarImagem(ImagemPet imagem);

    void removerImagem(String nomeArquivo);

    ImagemPet buscarImagem(String nomeArquivo);

    String gerarUrlAcesso(String nomeArquivo);
    
    byte[] buscarDadosImagem(String caminho);
}