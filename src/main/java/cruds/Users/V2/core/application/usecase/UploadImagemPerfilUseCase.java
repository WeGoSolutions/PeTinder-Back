package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.ArmazenamentoImagemGateway;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.command.UploadImagemCommand;
import cruds.Users.V2.core.application.exception.UsuarioException;
import cruds.Users.V2.core.domain.ImagemUsuario;
import cruds.Users.V2.core.domain.Usuario;
import cruds.common.util.ImageValidationUtil;

import java.io.IOException;

public class UploadImagemPerfilUseCase {
    private final UsuarioGateway usuarioGateway;
    private final ArmazenamentoImagemGateway armazenamentoImagemGateway;
    private static final String DEFAULT_IMAGE_NAME = "perfil.jpg";

    public UploadImagemPerfilUseCase(UsuarioGateway usuarioGateway,
                                   ArmazenamentoImagemGateway armazenamentoImagemGateway) {
        this.usuarioGateway = usuarioGateway;
        this.armazenamentoImagemGateway = armazenamentoImagemGateway;
    }

    public Usuario executar(UploadImagemCommand command) {
        // Buscar usuário
        Usuario usuario = usuarioGateway.buscarPorId(command.getUsuarioId())
            .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException(
                "Usuário não encontrado: " + command.getUsuarioId()
            ));

        // Criar imagem de usuário
        ImagemUsuario novaImagem = command.criarImagemUsuario();
        
        // Validar imagem usando o mesmo validador da V1
        try {
            ImageValidationUtil.validateUserImage(novaImagem.getDados(), DEFAULT_IMAGE_NAME);
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao processar a imagem: " + e.getMessage());
        }

        try {
            // Remover imagem anterior se existir
            if (usuario.getImagemUsuario() != null && usuario.getImagemUsuario().temImagem()) {
                try {
                    armazenamentoImagemGateway.removerImagem(usuario.getImagemUsuario().getNomeArquivo());
                } catch (Exception e) {
                    // Log do erro, mas não falha a operação
                    System.err.println("Erro ao remover imagem anterior: " + e.getMessage());
                }
            }

            // Salvar nova imagem
            String caminhoImagem = armazenamentoImagemGateway.salvarImagem(novaImagem);

            // Atualizar usuário com nova imagem
            usuario.atualizarImagemUsuario(novaImagem);

            // Salvar alterações
            return usuarioGateway.atualizar(usuario);
            
        } catch (Exception e) {
            throw new UsuarioException.ErroArmazenamentoException(
                "Erro ao processar upload da imagem: " + e.getMessage()
            );
        }
    }
}