package cruds.Users.application.service;

import cruds.Users.application.dto.mapper.UserDomainMapper;
import cruds.Users.application.service.interfaces.UserQueryServiceInterface;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.domain.factory.UserDomainFactory;
import cruds.Users.domain.model.UserDomain;
import cruds.Users.entity.User;
import cruds.Users.repository.UserRepository;
import cruds.common.exception.NoContentException;
import cruds.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Serviço de aplicação responsável por operações de consulta de usuários.
 * Refatorado para usar arquitetura baseada em POO com UserDomain.
 */
@Service
@Transactional(readOnly = true)
public class UserQueryService implements UserQueryServiceInterface {

    private final UserRepository userRepository;
    private final UserDomainFactory userDomainFactory;
    private final UserDomainMapper userDomainMapper;

    @Autowired
    public UserQueryService(
            UserRepository userRepository,
            UserDomainFactory userDomainFactory,
            UserDomainMapper userDomainMapper) {
        this.userRepository = userRepository;
        this.userDomainFactory = userDomainFactory;
        this.userDomainMapper = userDomainMapper;
    }

    /**
     * Lista todos os usuários do sistema usando domain objects
     */
    public List<UserResponseCadastroDTO> getAllUsers() {
        List<User> usuarios = userRepository.findAll();
        
        if (usuarios.isEmpty()) {
            throw new NoContentException("Nenhum usuário encontrado");
        }
        
        return usuarios.stream()
                .map(userDomainFactory::fromEntity)
                .map(userDomainMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca um usuário por ID usando domain objects
     */
    public UserResponseCadastroDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com id: " + id + " não encontrado"));
        
        UserDomain userDomain = userDomainFactory.fromEntity(user);
        return userDomainMapper.toResponseDto(userDomain);
    }

    /**
     * Valida se um email existe no sistema
     */
    public UserResponseCadastroDTO validateEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário com email " + email + " não encontrado"));
        
        UserDomain userDomain = userDomainFactory.fromEntity(user);
        return userDomainMapper.toResponseDto(userDomain);
    }

    /**
     * Busca usuário por email retornando domain object
     */
    public UserDomain findUserDomainByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário com email " + email + " não encontrado"));
        
        return userDomainFactory.fromEntity(user);
    }

    /**
     * Busca usuário por ID retornando domain object
     */
    public UserDomain findUserDomainById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com id " + id + " não encontrado"));
        
        return userDomainFactory.fromEntity(user);
    }

    /**
     * Busca usuário por email (método interno para compatibilidade)
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário com email " + email + " não encontrado"));
    }

    /**
     * Busca usuário por ID (método interno para compatibilidade)
     */
    public User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com id " + id + " não encontrado"));
    }

    /**
     * Verifica se um usuário existe por ID
     */
    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    /**
     * Verifica se um email já está em uso
     */
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Busca usuários por status usando domain objects
     */
    public List<UserResponseCadastroDTO> getUsersByStatus(boolean isNewUser) {
        List<User> usuarios = userRepository.findAll().stream()
                .filter(user -> (user.getUserNovo() != null ? user.getUserNovo() : true) == isNewUser)
                .collect(Collectors.toList());
        
        if (usuarios.isEmpty()) {
            throw new NoContentException("Nenhum usuário encontrado com o status especificado");
        }
        
        return usuarios.stream()
                .map(userDomainFactory::fromEntity)
                .map(userDomainMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca usuários por faixa etária usando domain objects
     */
    public List<UserResponseCadastroDTO> getUsersByAgeRange(int minAge, int maxAge) {
        List<User> usuarios = userRepository.findAll().stream()
                .filter(user -> {
                    UserDomain domain = userDomainFactory.fromEntity(user);
                    return domain.getBirthDate() != null && 
                           domain.getBirthDate().isInAgeRange(minAge, maxAge);
                })
                .collect(Collectors.toList());
        
        if (usuarios.isEmpty()) {
            throw new NoContentException("Nenhum usuário encontrado na faixa etária especificada");
        }
        
        return usuarios.stream()
                .map(userDomainFactory::fromEntity)
                .map(userDomainMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
