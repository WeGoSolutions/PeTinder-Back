package cruds.Users.domain.service;

import cruds.Users.domain.factory.UserDomainFactory;
import cruds.Users.domain.model.UserDomain;
import cruds.Users.entity.User;
import cruds.common.exception.ConflictException;
import cruds.common.exception.NotAllowedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Serviço de domínio responsável pelas regras de negócio relacionadas aos usuários.
 * Refatorado para usar arquitetura baseada em POO com UserDomain.
 */
@Service
public class UserDomainService {

    private final UserDomainFactory userDomainFactory;

    @Autowired
    public UserDomainService(UserDomainFactory userDomainFactory) {
        this.userDomainFactory = userDomainFactory;
    }

    /**
     * Valida se um usuário pode ser criado com os dados fornecidos
     */
    public UserDomain validateAndCreateUser(String nome, String email, String senha, LocalDate dataNasc) {
        // As validações agora são feitas pelos Value Objects no momento da criação
        UserDomain userDomain = userDomainFactory.createNewUser(nome, email, senha, dataNasc);
        return userDomain;
    }

    /**
     * Valida se um usuário pode ser atualizado
     */
    public void validateUserForUpdate(UserDomain userDomain, String nome, String email, LocalDate dataNasc) {
        if (userDomain == null) {
            throw new ConflictException("Usuário não encontrado para atualização");
        }
        
        if (!userDomain.canChangeEmail() && email != null && !email.equals(userDomain.getEmailAsString())) {
            throw new NotAllowedException("Usuário não pode alterar email no status atual");
        }
        
        // As demais validações são feitas pelos Value Objects
        if (nome != null || email != null || dataNasc != null) {
            userDomain.updatePersonalInfo(nome, email, dataNasc);
        }
    }

    /**
     * Verifica se dois usuários são a mesma pessoa
     */
    public boolean isSameUser(UserDomain user1, UserDomain user2) {
        if (user1 == null || user2 == null) {
            return false;
        }
        return user1.equals(user2);
    }

    /**
     * Determina se um usuário deve ser marcado como "novo"
     */
    public boolean shouldBeMarkedAsNewUser(UserDomain userDomain) {
        return userDomain != null && !userDomain.hasCompleteProfile();
    }

    /**
     * Processa a conclusão do perfil do usuário
     */
    public void completeUserProfile(UserDomain userDomain) {
        if (userDomain != null && userDomain.hasCompleteProfile()) {
            userDomain.markAsNotNewUser();
        }
    }

    /**
     * Valida se o usuário está em condições de usar o sistema
     */
    public void validateUserSystemEligibility(UserDomain userDomain) {
        if (userDomain == null) {
            throw new ConflictException("Usuário não encontrado");
        }
        
        if (!userDomain.isActive()) {
            throw new NotAllowedException("Usuário não está ativo no sistema");
        }
        
        if (userDomain.getBirthDate() != null && !userDomain.getBirthDate().isEligibleForSystem()) {
            throw new NotAllowedException("Usuário deve ter pelo menos 21 anos");
        }
    }

    /**
     * Converte User JPA para UserDomain
     */
    public UserDomain fromEntity(User user) {
        return userDomainFactory.fromEntity(user);
    }

    /**
     * Converte UserDomain para User JPA
     */
    public User toEntity(UserDomain userDomain) {
        return userDomainFactory.toEntity(userDomain);
    }

    /**
     * Atualiza entidade com dados do domínio
     */
    public void updateEntity(User entity, UserDomain domain) {
        userDomainFactory.updateEntity(entity, domain);
    }
}
