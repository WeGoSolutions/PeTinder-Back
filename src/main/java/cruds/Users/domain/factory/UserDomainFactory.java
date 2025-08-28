package cruds.Users.domain.factory;

import cruds.Users.domain.model.UserDomain;
import cruds.Users.entity.Endereco;
import cruds.Users.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Factory para criação de instâncias de UserDomain seguindo padrões de POO.
 * Centraliza a lógica de criação e conversão entre entidades.
 */
@Component
public class UserDomainFactory {

    /**
     * Cria um novo UserDomain para registro
     */
    public UserDomain createNewUser(String name, String email, String password, LocalDate birthDate) {
        return new UserDomain(name, email, password, birthDate);
    }

    /**
     * Cria UserDomain a partir de entidade JPA
     */
    public UserDomain fromEntity(User user) {
        if (user == null) {
            return null;
        }

        return new UserDomain(
            user.getId(),
            user.getNome(),
            user.getEmail(),
            user.getSenha(),
            user.getDataNasc(),
            user.getCpf(),
            user.getEndereco(),
            user.getUserNovo() != null ? user.getUserNovo() : true
        );
    }

    /**
     * Converte UserDomain para entidade JPA
     */
    public User toEntity(UserDomain userDomain) {
        if (userDomain == null) {
            return null;
        }

        User user = User.builder()
            .id(userDomain.getId())
            .nome(userDomain.getFullNameAsString())
            .email(userDomain.getEmailAsString())
            .senha(userDomain.getEncryptedPassword())
            .dataNasc(userDomain.getBirthDate() != null ? userDomain.getBirthDate().getDate() : null)
            .cpf(userDomain.getCpfUnformatted())
            .endereco(userDomain.getAddress())
            .userNovo(userDomain.isNewUser())
            .build();

        return user;
    }

    /**
     * Atualiza entidade JPA com dados do UserDomain
     */
    public void updateEntity(User entity, UserDomain domain) {
        if (entity == null || domain == null) {
            return;
        }

        entity.setNome(domain.getFullNameAsString());
        entity.setEmail(domain.getEmailAsString());
        entity.setSenha(domain.getEncryptedPassword());
        entity.setDataNasc(domain.getBirthDate() != null ? domain.getBirthDate().getDate() : null);
        entity.setCpf(domain.getCpfUnformatted());
        entity.setEndereco(domain.getAddress());
        entity.setUserNovo(domain.isNewUser());
    }

    /**
     * Cria um UserDomain para atualização de perfil
     */
    public UserDomain createForProfileUpdate(User existingUser, String name, String email, 
                                           LocalDate birthDate, String cpf, Endereco address) {
        UserDomain domain = fromEntity(existingUser);
        
        if (domain != null) {
            domain.updatePersonalInfo(name, email, birthDate);
            if (cpf != null) {
                domain.setCpf(cpf);
            }
            if (address != null) {
                domain.setAddress(address);
            }
        }
        
        return domain;
    }

    /**
     * Cria UserDomain apenas com informações básicas
     */
    public UserDomain createBasicUser(String name, String email, LocalDate birthDate) {
        UserDomain domain = new UserDomain(name, email, "", birthDate);
        return domain;
    }
}
