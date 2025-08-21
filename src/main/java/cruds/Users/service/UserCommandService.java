package cruds.Users.service;

import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import cruds.Users.controller.dto.request.UserRequestOptionalDTO;
import cruds.Users.controller.dto.request.UserRequestUpdateDTO;
import cruds.Users.entity.Endereco;
import cruds.Users.entity.User;
import cruds.Users.mapper.UserMapper;
import cruds.Users.repository.UserRepository;
import cruds.common.exception.ConflictException;
import cruds.common.exception.NotAllowedException;
import cruds.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserQueryService userQueryService;

    public User createUser(UserRequestCriarDTO dto) {
        validateUserCreationRules(dto);

        User user = userMapper.toEntity(dto);
        user.setSenha(passwordEncoder.encode(dto.getSenha()));
        // Disparar evento de boas-vindas aqui
        return userRepository.save(user);
    }

    public User updateUser(Integer id, UserRequestUpdateDTO dto) {
        validateUserAge(dto.getDataNasc());
        User user = userQueryService.findUserById(id);

        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setDataNasc(dto.getDataNasc());
        user.setCpf(dto.getCpf());

        // Lógica para Endereço
        Endereco endereco = user.getEndereco() != null ? user.getEndereco() : new Endereco();
        endereco.setCep(dto.getCep());
        endereco.setRua(dto.getRua());
        // ... resto dos campos de endereço
        user.setEndereco(endereco);

        return userRepository.save(user);
    }

    public User updateOptionalInfo(Integer id, UserRequestOptionalDTO dto) {
        User user = userQueryService.findUserById(id);
        user.setCpf(dto.getCpf());

        Endereco endereco = user.getEndereco() != null ? user.getEndereco() : new Endereco();
        endereco.setCep(dto.getCep());
        // ... resto dos campos
        user.setEndereco(endereco);

        return userRepository.save(user);
    }

    public User setUserNovoToFalse(Integer id) {
        User user = userQueryService.findUserById(id);
        user.setUserNovo(false);
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Usuário com id: " + id + " não encontrado");
        }
        // Disparar evento para deletar dados relacionados (ex: PetStatus)
        userRepository.deleteById(id);
    }

    public void deleteAllUsers() {
        // Lógica para deletar dados relacionados primeiro
        userRepository.deleteAll();
    }

    private void validateUserCreationRules(UserRequestCriarDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ConflictException("Email já cadastrado");
        }
        validateUserAge(dto.getDataNasc());
        // Outras validações de senha, nome, etc.
    }

    private void validateUserAge(LocalDate dataNasc) {
        if (dataNasc == null || Period.between(dataNasc, LocalDate.now()).getYears() < 21) {
            throw new NotAllowedException("Usuário deve ter 21 anos ou mais.");
        }
    }
}