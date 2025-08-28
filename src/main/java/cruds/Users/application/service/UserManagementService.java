package cruds.Users.application.service;

import cruds.Pets.repository.PetStatusRepository;
import cruds.Users.application.facade.UserAccountFacade;
import cruds.Users.application.facade.UserDataFacade;
import cruds.Users.application.facade.UserProfileFacade;
import cruds.Users.application.service.interfaces.UserManagementServiceInterface;
import cruds.Users.controller.dto.request.*;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.controller.dto.response.UserResponseLoginDTO;
import cruds.Users.controller.dto.response.UserResponseUrlDTO;
import cruds.Users.entity.User;
import cruds.Users.repository.UserRepository;
import cruds.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Serviço principal de gerenciamento de usuários.
 * Refatorado para usar Facades que agrupam operações relacionadas.
 * Este serviço agora atua como um ponto de entrada unificado para o controller.
 * 
 * Arquitetura melhorada:
 * - UserAccountFacade: Cadastro + Login
 * - UserProfileFacade: Perfil + Senhas  
 * - UserDataFacade: Consultas + Validações
 */
@Service
@Transactional
public class UserManagementService implements UserManagementServiceInterface {

    private final UserAccountFacade userAccountFacade;
    private final UserProfileFacade userProfileFacade;
    private final UserDataFacade userDataFacade;
    private final UserRepository userRepository;
    private final PetStatusRepository petStatusRepository;

    @Autowired
    public UserManagementService(
            UserAccountFacade userAccountFacade,
            UserProfileFacade userProfileFacade,
            UserDataFacade userDataFacade,
            UserRepository userRepository,
            PetStatusRepository petStatusRepository) {
        this.userAccountFacade = userAccountFacade;
        this.userProfileFacade = userProfileFacade;
        this.userDataFacade = userDataFacade;
        this.userRepository = userRepository;
        this.petStatusRepository = petStatusRepository;
    }

    // ===== OPERAÇÕES DE REGISTRO =====
    
    public UserResponseCadastroDTO createUser(UserRequestCriarDTO dto) {
        return userAccountFacade.createUser(dto);
    }

    // ===== OPERAÇÕES DE AUTENTICAÇÃO =====
    
    public UserResponseLoginDTO login(String email, String senha) {
        return userAccountFacade.login(email, senha);
    }

    public UserRequestTokenDto autenticar(User usuario) {
        return userAccountFacade.autenticar(usuario);
    }

    // ===== OPERAÇÕES DE CONSULTA =====
    
    public List<UserResponseCadastroDTO> getListaUsuarios() {
        return userDataFacade.getListaUsuarios();
    }

    public UserResponseCadastroDTO getUserById(UUID id) {
        return userDataFacade.getUserById(id);
    }

    public UserResponseCadastroDTO validarEmail(String email) {
        return userDataFacade.validarEmail(email);
    }

    // ===== OPERAÇÕES DE PERFIL =====
    
    public UserResponseCadastroDTO updateOptionalInfo(UUID id, UserRequestOptionalDTO dto) {
        return userProfileFacade.updateOptionalInfo(id, dto);
    }

    public UserResponseCadastroDTO updateUser(UUID id, UserRequestUpdateDTO dto) {
        return userProfileFacade.updateUser(id, dto);
    }

    public UserResponseCadastroDTO atualizarUserNovoParaFalse(UUID id) {
        return userProfileFacade.atualizarUserNovoParaFalse(id);
    }

    // ===== OPERAÇÕES DE SENHA =====
    
    public UserResponseCadastroDTO updatePassword(UUID id, String senhaAtual, String novaSenha) {
        return userProfileFacade.updatePassword(id, senhaAtual, novaSenha);
    }

    public UserResponseCadastroDTO updateSenha(String email, UserRequestSenhaDTO senhaDto) {
        return userProfileFacade.updateSenha(email, senhaDto);
    }

    // ===== OPERAÇÕES DE EXCLUSÃO =====
    
    public void deleteUser(UUID id) {
        if (!userDataFacade.existsById(id)) {
            throw new NotFoundException("Usuário com id: " + id + " não encontrado");
        }
        
        // Remover dependências
        petStatusRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    /**
     * Deleta todos os usuários - APENAS PARA DESENVOLVIMENTO/TESTES
     * @deprecated Este método deve ser removido em produção por questões de segurança
     */
    @Deprecated
    public void deleteAllUsers() {
        // Remover todas as dependências primeiro
        petStatusRepository.deleteAll();
        // Depois remover todos os usuários
        userRepository.deleteAll();
    }

    // ===== MÉTODOS TEMPORÁRIOS PARA MANTER COMPATIBILIDADE =====
    // TODO: Estes métodos devem ser removidos quando a migração de imagens for concluída
    
    /**
     * @deprecated Este método será removido quando a funcionalidade de imagens for migrada para Python
     */
    @Deprecated
    public UserResponseCadastroDTO updateImagemPerfil(UUID id, UserRequestImagemPerfilDTO dto) {
        throw new UnsupportedOperationException("Funcionalidade de imagem será migrada para serviço Python");
    }

    /**
     * @deprecated Este método será removido quando a funcionalidade de imagens for migrada para Python
     */
    @Deprecated
    public UserResponseCadastroDTO uploadImagemPerfil(UUID id, UserRequestImagemPerfilDTO dto) {
        throw new UnsupportedOperationException("Funcionalidade de imagem será migrada para serviço Python");
    }

    /**
     * @deprecated Este método será removido quando a funcionalidade de imagens for migrada para Python
     */
    @Deprecated
    public UserResponseCadastroDTO deleteImagemPerfil(UUID id) {
        throw new UnsupportedOperationException("Funcionalidade de imagem será migrada para serviço Python");
    }

    /**
     * @deprecated Este método será removido quando a funcionalidade de imagens for migrada para Python
     */
    @Deprecated
    public byte[] getImagemPorIndice(UUID userId, int indice) {
        throw new UnsupportedOperationException("Funcionalidade de imagem será migrada para serviço Python");
    }

    /**
     * @deprecated Este método será removido quando a funcionalidade de imagens for migrada para Python
     */
    @Deprecated
    public UserResponseUrlDTO getUrlImageUser(UUID id) {
        throw new UnsupportedOperationException("Funcionalidade de imagem será migrada para serviço Python");
    }
}
