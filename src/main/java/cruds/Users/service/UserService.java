package cruds.Users.service;

import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import cruds.Users.controller.dto.request.UserRequestImagemPerfilDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.entity.User;
import cruds.Users.exceptions.UserNotFoundException;
import cruds.Users.exceptions.UserVazioException;
import cruds.Users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseLoginDTO createUser(UserRequestCriarDTO dto) {
        User user = UserRequestCriarDTO.toEntity(dto);
        User savedUser = userRepository.save(user);
        return convertEntityToResponse(savedUser);
    }

    public List<UserResponseLoginDTO> listarUsuarios() {
        var usuarios = userRepository.findAll();
        if (usuarios.isEmpty()) {
            throw new UserVazioException("Nenhum usuário encontrado");
        }
        return usuarios.stream()
                .map(this::convertEntityToResponse)
                .collect(Collectors.toList());
    }

    public UserResponseLoginDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário com id: " + id + " não encontrado"));
        return convertEntityToResponse(user);
    }

    public UserResponseLoginDTO updateUser(Integer id, UserRequestCriarDTO dto) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuário com id: " + id + " não encontrado");
        }
        User user = UserRequestCriarDTO.toEntity(dto);
        user.setId(id);
        User updatedUser = userRepository.save(user);
        return convertEntityToResponse(updatedUser);
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuário com id: " + id + " não encontrado");
        }
        userRepository.deleteById(id);
    }

    public UserResponseLoginDTO updateImagemPerfil(Integer id, UserRequestImagemPerfilDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário com id: " + id + " não encontrado"));
        user.setImagemUsuario(dto.getImagemDecodificada());
        User updatedUser = userRepository.save(user);
        return convertEntityToResponse(updatedUser);
    }

    private UserResponseLoginDTO convertEntityToResponse(User user) {
        return UserResponseLoginDTO.builder()
                .nome(user.getNome())
                .email(user.getEmail())
                .build();
    }
}