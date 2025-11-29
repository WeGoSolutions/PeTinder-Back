package cruds.Users.V2.infrastructure.gateway;

import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.domain.Usuario;
import cruds.Users.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserGatewayImpl implements UsuarioGateway {

    private final UserRepository userRepository;

    public UserGatewayImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        return null;
    }

    @Override
    public Usuario atualizar(Usuario usuario) {
        return null;
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> buscarPorEmailESenha(String email, String senha) {
        return Optional.empty();
    }

    @Override
    public List<Usuario> listarTodos() {
        return List.of();
    }

    @Override
    public void remover(UUID id) {

    }

    @Override
    public boolean emailJaExiste(String email) {
        return false;
    }

    @Override
    public boolean cpfJaExiste(String cpf) {
        return false;
    }

    @Override
    public boolean existePorId(UUID id) {
        return userRepository.existsById(id);
    }

    @Override
    public void removerTodos() {

    }
}