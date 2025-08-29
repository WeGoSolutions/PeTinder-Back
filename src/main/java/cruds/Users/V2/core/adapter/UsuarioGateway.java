package cruds.Users.V2.core.adapter;

import cruds.Users.V2.core.domain.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioGateway {

    Usuario salvar(Usuario usuario);

    Usuario atualizar(Usuario usuario);

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorEmail(String email);

    Optional<Usuario> buscarPorEmailESenha(String email, String senha);

    List<Usuario> listarTodos();

    void remover(Long id);

    boolean emailJaExiste(String email);

    boolean cpfJaExiste(String cpf);

    void removerTodos();
}
