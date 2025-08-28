package cruds.Users.application.service;

import cruds.Users.application.dto.mapper.UserDomainMapper;
import cruds.Users.application.service.interfaces.UserProfileServiceInterface;
import cruds.Users.controller.dto.request.UserRequestOptionalDTO;
import cruds.Users.controller.dto.request.UserRequestUpdateDTO;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.domain.factory.UserDomainFactory;
import cruds.Users.domain.model.UserDomain;
import cruds.Users.domain.service.UserDomainService;
import cruds.Users.entity.Endereco;
import cruds.Users.entity.User;
import cruds.Users.repository.UserRepository;
import cruds.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Serviço de aplicação responsável pelo gerenciamento de perfil dos usuários.
 * Refatorado para usar arquitetura baseada em POO com UserDomain.
 */
@Service
@Transactional
public class UserProfileService implements UserProfileServiceInterface {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final UserDomainFactory userDomainFactory;
    private final UserDomainMapper userDomainMapper;

    @Autowired
    public UserProfileService(
            UserRepository userRepository,
            UserDomainService userDomainService,
            UserDomainFactory userDomainFactory,
            UserDomainMapper userDomainMapper) {
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
        this.userDomainFactory = userDomainFactory;
        this.userDomainMapper = userDomainMapper;
    }

    /**
     * Atualiza informações opcionais do usuário usando domain objects
     */
    public UserResponseCadastroDTO updateOptionalInfo(UUID id, UserRequestOptionalDTO dto) {
        User user = findUserById(id);
        UserDomain userDomain = userDomainFactory.fromEntity(user);
        
        // Atualizar CPF se fornecido
        if (dto.getCpf() != null) {
            userDomain.setCpf(dto.getCpf());
        }
        
        // Criar ou atualizar endereço
        Endereco endereco = createOrUpdateAddress(user.getEndereco(), dto);
        userDomain.setAddress(endereco);
        
        // Verificar se o perfil está completo
        userDomainService.completeUserProfile(userDomain);
        
        // Atualizar entidade e salvar
        userDomainFactory.updateEntity(user, userDomain);
        User updatedUser = userRepository.save(user);
        
        // Converter para response
        UserDomain updatedDomain = userDomainFactory.fromEntity(updatedUser);
        return userDomainMapper.toResponseDto(updatedDomain);
    }

    /**
     * Atualiza informações completas do usuário usando domain objects
     */
    public UserResponseCadastroDTO updateUser(UUID id, UserRequestUpdateDTO dto) {
        validateUserExists(id);
        
        User user = findUserById(id);
        UserDomain userDomain = userDomainFactory.fromEntity(user);
        
        // Converter DTO para dados de atualização
        UserDomainMapper.UserUpdateData updateData = userDomainMapper.fromUpdateDto(dto);
        
        // Validar e atualizar informações pessoais
        userDomainService.validateUserForUpdate(
            userDomain,
            updateData.getName(),
            updateData.getEmail(),
            updateData.getBirthDate()
        );
        
        // Atualizar CPF se fornecido
        if (updateData.getCpf() != null) {
            userDomain.setCpf(updateData.getCpf());
        }
        
        // Criar ou atualizar endereço
        Endereco endereco = createAddressFromUpdateData(user.getEndereco(), updateData);
        userDomain.setAddress(endereco);
        
        // Verificar se o perfil está completo
        userDomainService.completeUserProfile(userDomain);
        
        // Atualizar entidade e salvar
        userDomainFactory.updateEntity(user, userDomain);
        User updatedUser = userRepository.save(user);
        
        // Converter para response
        UserDomain updatedDomain = userDomainFactory.fromEntity(updatedUser);
        return userDomainMapper.toResponseDto(updatedDomain);
    }

    /**
     * Marca o usuário como não sendo mais novo
     */
    public UserResponseCadastroDTO markUserAsNotNew(UUID id) {
        User user = findUserById(id);
        UserDomain userDomain = userDomainFactory.fromEntity(user);
        
        userDomain.markAsNotNewUser();
        
        userDomainFactory.updateEntity(user, userDomain);
        User updatedUser = userRepository.save(user);
        
        UserDomain updatedDomain = userDomainFactory.fromEntity(updatedUser);
        return userDomainMapper.toResponseDto(updatedDomain);
    }

    /**
     * Método de compatibilidade - delega para markUserAsNotNew
     * @deprecated Use markUserAsNotNew em vez deste método
     */
    @Deprecated
    public UserResponseCadastroDTO atualizarUserNovoParaFalse(UUID id) {
        return markUserAsNotNew(id);
    }

    /**
     * Cria ou atualiza endereço a partir de DTO opcional
     */
    private Endereco createOrUpdateAddress(Endereco existingAddress, UserRequestOptionalDTO dto) {
        Endereco endereco = existingAddress != null ? existingAddress : new Endereco();
        
        endereco.setCep(dto.getCep());
        endereco.setRua(dto.getRua());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setCidade(dto.getCidade());
        endereco.setUf(dto.getUf());
        
        return endereco;
    }

    /**
     * Cria endereço a partir de dados de atualização
     */
    private Endereco createAddressFromUpdateData(Endereco existingAddress, UserDomainMapper.UserUpdateData updateData) {
        Endereco endereco = existingAddress != null ? existingAddress : new Endereco();
        
        endereco.setCep(updateData.getCep());
        endereco.setRua(updateData.getRua());
        endereco.setNumero(updateData.getNumero());
        endereco.setComplemento(updateData.getComplemento());
        endereco.setCidade(updateData.getCidade());
        endereco.setUf(updateData.getUf());
        
        return endereco;
    }

    private User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com id " + id + " não encontrado"));
    }

    private void validateUserExists(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Usuário com id: " + id + " não encontrado");
        }
    }
}
