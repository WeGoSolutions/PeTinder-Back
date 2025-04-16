// linguagem: java
package cruds.Users.service;

import cruds.common.event.UserCreatedEvent;
import cruds.common.exception.*;
import org.springframework.context.ApplicationEventPublisher;
import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import cruds.Users.controller.dto.request.UserRequestImagemPerfilDTO;
import cruds.Users.controller.dto.request.UserRequestOptionalDTO;
import cruds.Users.controller.dto.request.UserRequestSenhaDTO;
import cruds.Users.controller.dto.request.UserRequestUpdateDTO;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.entity.Endereco;
import cruds.Users.entity.ImagemUser;
import cruds.Users.entity.User;
import cruds.Users.repository.UserRepository;
import cruds.common.util.ImageValidationUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private static final String DEFAULT_IMAGE_NAME = "perfil.jpg"; //alterar para a imagem default

    @Autowired
    public UserService(UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public UserResponseCadastroDTO createUser(UserRequestCriarDTO dto) {
        userRules(dto);
        User user = UserRequestCriarDTO.toEntity(dto);
        User savedUser = userRepository.save(user);
        UserResponseCadastroDTO response = UserResponseCadastroDTO.toResponse(savedUser);
        eventPublisher.publishEvent(new UserCreatedEvent(this, response));
        return response;
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
            throw new NotFoundException("Email não encontrado");
        }

        User user = userOptional.get();
        if (!user.getSenha().equals(senha)) {
            throw new NotFoundException("Senha inválida");
        }

        return UserResponseLoginDTO.toResponse(user);
    }

    public List<UserResponseCadastroDTO> getListaUsuarios() {
        var usuarios = userRepository.findAll();
        if (usuarios.isEmpty()) {
            throw new NoContentException("Nenhum usuário encontrado");
        }
        return usuarios.stream()
                .map(UserResponseCadastroDTO::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponseCadastroDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com id: " + id + " não encontrado"));
        return UserResponseCadastroDTO.toResponse(user);
    }

    public UserResponseCadastroDTO updateUser(Integer id, UserRequestUpdateDTO dto) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Usuário com id: " + id + " não encontrado");
        }
        if (!dto.isMaiorDe21()) {
            throw new NotAllowedException("A pessoa deve ter mais de 21 anos");
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
        user.setCpf(dto.getCpf());

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

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Usuário com id: " + id + " não encontrado");
        }
        userRepository.deleteById(id);
    }

    public UserResponseCadastroDTO updateImagemPerfil(Integer id, UserRequestImagemPerfilDTO dto) {
        byte[] imagemDecodificada = dto.getImagemDecodificada();
        try {
            ImageValidationUtil.validateUserImage(imagemDecodificada, DEFAULT_IMAGE_NAME);
        } catch (IOException e) {
            throw new BadRequestException("Erro ao processar a imagem: " + e.getMessage());
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
            throw new BadRequestException("Erro ao processar a imagem: " + e.getMessage());
        }

        User user = getUsuarioPorId(id);
        user.setImagemUser(new ImagemUser(imagemDecodificada));
        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    public UserResponseCadastroDTO deleteImagemPerfil(Integer id) {
        User user = getUsuarioPorId(id);
        if (user.getImagemUser() == null) {
            throw new BadRequestException("Usuário não possui uma imagem de perfil para remover.");
        }
        user.setImagemUser(null);
        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    public byte[] getImagemPorIndice(Integer userId, int indice) {
        User user = getUsuarioPorId(userId);
        if (user.getImagemUser() == null || indice != 0) {
            throw new NotFoundException("Imagem não encontrada para o usuário com id " + userId);
        }
        return user.getImagemUser().getDados();
    }

    public UserResponseCadastroDTO updateSenha(String email, @Valid UserRequestSenhaDTO senha) {

        User user = getUsuarioPorEmail(email);
        user.setSenha(senha.getSenha());
        return UserResponseCadastroDTO.toResponse(userRepository.save(user));

    }

    public UserResponseCadastroDTO validarEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário com email " + email + " não encontrado"));
        return UserResponseCadastroDTO.toResponse(user);
    }

    private User getUsuarioPorId(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com id " + id + " não encontrado"));
    }

    private User getUsuarioPorEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário com email " + email + " não encontrado"));
    }

    private void userRules(UserRequestCriarDTO user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@") || userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ConflictException("Email não pode ser utilizado");
        }
        if (user.getSenha() == null || user.getSenha().isEmpty() || user.getSenha().length() < 8
                || !user.getSenha().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$")) {
            throw new ConflictException("Senha não pode ser cadastrada ou não atende aos requisitos de segurança");
        }
        if (user.getNome() == null || user.getNome().isEmpty() || user.getNome().length() < 3
                || !user.getNome().matches("^[A-Za-zÀ-Ö ]+$")) {
            throw new ConflictException("Nome não pode ser utilizado");
        }

        user.isMaiorDe21();
    }
}