package cruds.Users.service;

import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import cruds.Users.controller.dto.request.UserRequestImagemPerfilDTO;
import cruds.Users.controller.dto.request.UserRequestOptionalDTO;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.entity.User;
import cruds.Users.exceptions.*;
import cruds.Users.repository.UserRepository;
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
    private static final String[] TIPOS_PERMITIDOS = {"jpg", "jpeg", "png"};
    private static final int TAMANHO_MAXIMO = 5 * 1024 * 1024;

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

        user.setCpf(dto.getCpf());
        user.setCep(dto.getCep());
        user.setRua(dto.getRua());
        user.setNumero(dto.getNumero());
        user.setComplemento(dto.getComplemento());
        user.setCidade(dto.getCidade());
        user.setUf(dto.getUf());

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

        if (dto.getSenha() == null || dto.getSenha().isBlank() || dto.getSenha().length() < 6 || !dto.getSenha().matches("^(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z0-9!@#$%^&*(),.?\":{}|<>]+$")) {
            throw new ConflictException("Senha inválida: deve ter no mínimo 6 caracteres e conter pelo menos um caractere especial");
        }

        if (dto.getNome() == null || dto.getNome().isBlank() || dto.getNome().length() < 3 || !dto.getNome().matches("^[A-Za-zÀ-Ö ]+$")) {
            throw new ConflictException("Nome inválido: deve ter no mínimo 3 caracteres e conter apenas letras e espaços");
        }

        if (dto.getCpf() == null || !dto.getCpf().matches("\\d{11}")) {
            throw new ConflictException("CPF inválido: deve conter exatamente 11 dígitos");
        }
        if (dto.getCep() == null || !dto.getCep().matches("\\d{8}")) {
            throw new ConflictException("CEP inválido: deve conter exatamente 8 dígitos");
        }

        User user = UserRequestCriarDTO.toEntity(dto);
        user.setId(id);
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
        User user = getUsuarioPorId(id);
        user.setImagemUsuario(dto.getImagemDecodificada());
        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    public UserResponseCadastroDTO uploadImagemPerfil(Integer id, UserRequestImagemPerfilDTO dto) {
        byte[] imagemDecodificada = dto.getImagemDecodificada();
        try {
            validarImagem(imagemDecodificada, dto.getImagemUsuario());
        } catch (IOException e) {
            throw new ImagemUploadException("Erro ao processar a imagem: " + e.getMessage());
        }

        User user = getUsuarioPorId(id);
        user.setImagemUsuario(imagemDecodificada);
        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    public UserResponseCadastroDTO deleteImagemPerfil(Integer id) {
        User user = getUsuarioPorId(id);
        if (user.getImagemUsuario().equals("data:image/jpeg;base64")) {
            throw new ImagemUploadException("Usuário não possui uma imagem de perfil para remover.");
        }

        user.setImagemUsuario(null);
        User updatedUser = userRepository.save(user);
        return UserResponseCadastroDTO.toResponse(updatedUser);
    }

    private User getUsuarioPorId(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário com id: " + id + " não encontrado"));
    }

    private void validarImagem(byte[] imagemDecodificada, String imagemUsuario) throws IOException {
        String tipoArquivo = FilenameUtils.getExtension(imagemUsuario);
        if (!Arrays.asList(TIPOS_PERMITIDOS).contains(tipoArquivo.toLowerCase())) {
            throw new ImagemUploadException("Tipo de arquivo não permitido. Apenas JPG e PNG são aceitos.");
        }
        if (imagemDecodificada.length > TAMANHO_MAXIMO) {
            throw new ImagemUploadException("Tamanho da imagem excede o limite de 5MB.");
        }
        if (ImageIO.read(new ByteArrayInputStream(imagemDecodificada)) == null) {
            throw new ImagemUploadException("Arquivo enviado não é uma imagem válida.");
        }
    }
}