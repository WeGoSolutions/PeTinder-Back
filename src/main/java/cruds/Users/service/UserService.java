// linguagem: java
package cruds.Users.service;

import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import cruds.Users.controller.dto.request.UserRequestImagemPerfilDTO;
import cruds.Users.controller.dto.request.UserRequestOptionalDTO;
import cruds.Users.controller.dto.request.UserRequestSenhaDTO;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.entity.Endereco;
import cruds.Users.entity.ImagemUser;
import cruds.Users.entity.User;
import cruds.Users.exceptions.*;
import cruds.Users.repository.UserRepository;
import cruds.common.util.ImageValidationUtil;
import jakarta.validation.Valid;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final String DEFAULT_IMAGE_NAME = "perfil.jpg";

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseCadastroDTO createUser(UserRequestCriarDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ConflictException("Email já cadastrado: " + dto.getEmail());
        }
        if (!dto.isMaiorDe21()) {
            throw new IdadeMenorException("A pessoa deve ter mais de 21 anos");
        }
        User user = UserRequestCriarDTO.toEntity(dto);
        User savedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(savedUser);
    }

    public UserResponseCadastroDTO updateOptionalInfo(Integer id, UserRequestOptionalDTO dto) {
        User user = getUsuarioPorId(id);

        if (dto.getCpf() != null) {
            user.setCpf(dto.getCpf());
        }

        Endereco endereco = (user.getEndereco() != null) ? user.getEndereco() : new Endereco();
        endereco.setCep(dto.getCep());
        endereco.setRua(dto.getRua());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setCidade(dto.getCidade());
        endereco.setUf(dto.getUf());
        user.setEndereco(endereco);

        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    public UserResponseLoginDTO login(String email, String senha) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("Email não encontrado");
        }

        User user = userOptional.get();
        if (!user.getSenha().equals(senha)) {
            throw new UserNotFoundException("Senha inválida");
        }

        return UserResponseLoginDTO.toResponse(user);
    }

    public List<UserResponseCadastroDTO> getListaUsuarios() {
        var usuarios = userRepository.findAll();
        if (usuarios.isEmpty()) {
            throw new UserVazioException("Nenhum usuário encontrado");
        }
        return usuarios.stream()
                .map(UserResponseCadastroDTO::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponseCadastroDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário com id: " + id + " não encontrado"));
        return UserResponseCadastroDTO.toResponse(user);
    }

    public UserResponseCadastroDTO updateUser(Integer id, UserRequestCriarDTO dto) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuário com id: " + id + " não encontrado");
        }
        if (!dto.isMaiorDe21()) {
            throw new IdadeMenorException("A pessoa deve ter mais de 21 anos");
        }
        Optional<User> existingUserOptional = userRepository.findByEmail(dto.getEmail());
        if (existingUserOptional.isPresent() && !existingUserOptional.get().getId().equals(id)) {
            throw new ConflictException("Email já cadastrado: " + dto.getEmail());
        }
        User user = getUsuarioPorId(id);
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setSenha(dto.getSenha());
        user.setDataNasc(dto.getDataNasc());
        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuário com id: " + id + " não encontrado");
        }
        userRepository.deleteById(id);
    }

    public UserResponseCadastroDTO updateImagemPerfil(Integer id, UserRequestImagemPerfilDTO dto) {
        byte[] imagemDecodificada = dto.getImagemDecodificada();
        try {
            ImageValidationUtil.validateUserImage(imagemDecodificada, DEFAULT_IMAGE_NAME);
        } catch (IOException e) {
            throw new ImagemUploadException("Erro ao processar a imagem: " + e.getMessage());
        }

        User user = getUsuarioPorId(id);
        if (user.getImagemUser() != null) {
            user.getImagemUser().setDados(imagemDecodificada);
        } else {
            user.setImagemUser(new ImagemUser(imagemDecodificada));
        }
        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    public UserResponseCadastroDTO uploadImagemPerfil(Integer id, UserRequestImagemPerfilDTO dto) {
        byte[] imagemDecodificada = dto.getImagemDecodificada();
        try {
            ImageValidationUtil.validateUserImage(imagemDecodificada, DEFAULT_IMAGE_NAME);
        } catch (IOException e) {
            throw new ImagemUploadException("Erro ao processar a imagem: " + e.getMessage());
        }

        User user = getUsuarioPorId(id);
        user.setImagemUser(new ImagemUser(imagemDecodificada));
        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    public UserResponseCadastroDTO deleteImagemPerfil(Integer id) {
        User user = getUsuarioPorId(id);
        if (user.getImagemUser() == null) {
            throw new ImagemUploadException("Usuário não possui uma imagem de perfil para remover.");
        }
        user.setImagemUser(null);
        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    public UserResponseCadastroDTO updateSenha(Integer id, @Valid UserRequestSenhaDTO senha) {

        User user = getUsuarioPorId(id);
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            throw new UserEmailNotFoundException("Esse email não existe");
        }
        user.setSenha(senha.getSenha());
        return UserResponseCadastroDTO.toResponse(userRepository.save(user));

    }

    private User getUsuarioPorId(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário com id: " + id + " não encontrado"));
    }

}