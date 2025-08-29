package cruds.Users.V2.core.adapter;

import cruds.Users.V2.core.domain.ImagemUsuario;

public interface ArmazenamentoImagemGateway {

    String salvarImagem(ImagemUsuario imagem);

    void removerImagem(String nomeArquivo);

    ImagemUsuario buscarImagem(String nomeArquivo);

    String gerarUrlAcesso(String nomeArquivo);
}
