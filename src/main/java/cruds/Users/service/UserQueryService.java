package cruds.Users.service;

import cruds.Users.entity.User;
import cruds.Users.repository.UserRepository;
import cruds.common.exception.NoContentException;
import cruds.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    public User findUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com id: " + id + " não encontrado"));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário com email " + email + " não encontrado"));
    }

    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new NoContentException("Nenhum usuário encontrado");
        }
        return users;
    }
}