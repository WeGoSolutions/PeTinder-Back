package cruds.Users.domain.model;

import cruds.Users.domain.model.valueobject.BirthDate;
import cruds.Users.domain.model.valueobject.Cpf;
import cruds.Users.domain.model.valueobject.Email;
import cruds.Users.domain.model.valueobject.FullName;
import cruds.Users.entity.Endereco;
import cruds.common.exception.ConflictException;
import cruds.common.exception.NotAllowedException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade de domínio User seguindo princípios de DDD e POO.
 * Encapsula comportamentos e regras de negócio relacionadas ao usuário.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDomain {

    private UUID id;
    private FullName fullName;
    private Email email;
    private String encryptedPassword;
    private BirthDate birthDate;
    private Cpf cpf;
    private Endereco address;
    private boolean isNewUser;
    private UserStatus status;

    // Construtor para novo usuário
    public UserDomain(String name, String email, String password, LocalDate birthDate) {
        this.fullName = new FullName(name);
        this.email = new Email(email);
        this.birthDate = new BirthDate(birthDate);
        this.status = UserStatus.ACTIVE;
        this.isNewUser = true;
        
        validateUserEligibility();
    }

    // Construtor completo
    public UserDomain(UUID id, String name, String email, String encryptedPassword, 
                     LocalDate birthDate, String cpf, Endereco address, boolean isNewUser) {
        this.id = id;
        this.fullName = new FullName(name);
        this.email = new Email(email);
        this.encryptedPassword = encryptedPassword;
        this.birthDate = new BirthDate(birthDate);
        this.cpf = cpf != null ? new Cpf(cpf) : null;
        this.address = address;
        this.isNewUser = isNewUser;
        this.status = UserStatus.ACTIVE;
        
        validateUserEligibility();
    }

    /**
     * Valida se o usuário é elegível para o sistema
     */
    private void validateUserEligibility() {
        if (!birthDate.isEligibleForSystem()) {
            throw new NotAllowedException("Usuário deve ter pelo menos 21 anos para usar o sistema");
        }
    }

    /**
     * Define a senha criptografada
     */
    public void setEncryptedPassword(String encryptedPassword) {
        if (encryptedPassword == null || encryptedPassword.isEmpty()) {
            throw new ConflictException("Senha criptografada não pode ser vazia");
        }
        this.encryptedPassword = encryptedPassword;
    }

    /**
     * Define o CPF do usuário
     */
    public void setCpf(String cpf) {
        this.cpf = cpf != null ? new Cpf(cpf) : null;
    }

    /**
     * Define o endereço do usuário
     */
    public void setAddress(Endereco address) {
        this.address = address;
        updateNewUserStatus();
    }

    /**
     * Atualiza informações pessoais
     */
    public void updatePersonalInfo(String name, String email, LocalDate birthDate) {
        if (name != null) {
            this.fullName = new FullName(name);
        }
        if (email != null) {
            this.email = new Email(email);
        }
        if (birthDate != null) {
            this.birthDate = new BirthDate(birthDate);
            validateUserEligibility();
        }
    }

    /**
     * Marca o usuário como não sendo mais novo
     */
    public void markAsNotNewUser() {
        this.isNewUser = false;
    }

    /**
     * Atualiza status baseado no perfil completude
     */
    private void updateNewUserStatus() {
        if (hasCompleteProfile()) {
            this.isNewUser = false;
        }
    }

    /**
     * Verifica se o perfil está completo
     */
    public boolean hasCompleteProfile() {
        return fullName != null && 
               email != null && 
               birthDate != null && 
               address != null && 
               address.getCep() != null && 
               !address.getCep().isEmpty();
    }

    /**
     * Verifica se o usuário pode alterar email
     */
    public boolean canChangeEmail() {
        return status == UserStatus.ACTIVE;
    }

    /**
     * Ativa o usuário
     */
    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    /**
     * Desativa o usuário
     */
    public void deactivate() {
        this.status = UserStatus.INACTIVE;
    }

    /**
     * Suspende o usuário
     */
    public void suspend() {
        this.status = UserStatus.SUSPENDED;
    }

    /**
     * Verifica se o usuário está ativo
     */
    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    /**
     * Obtém a idade do usuário
     */
    public int getAge() {
        return birthDate != null ? birthDate.getAge() : 0;
    }

    /**
     * Obtém o nome completo como string
     */
    public String getFullNameAsString() {
        return fullName != null ? fullName.getFullName() : "";
    }

    /**
     * Obtém o email como string
     */
    public String getEmailAsString() {
        return email != null ? email.getValue() : "";
    }

    /**
     * Obtém o CPF formatado
     */
    public String getCpfFormatted() {
        return cpf != null ? cpf.getFormatted() : null;
    }

    /**
     * Obtém o CPF sem formatação
     */
    public String getCpfUnformatted() {
        return cpf != null ? cpf.getUnformatted() : null;
    }

    /**
     * Enum para status do usuário
     */
    public enum UserStatus {
        ACTIVE("Ativo"),
        INACTIVE("Inativo"),
        SUSPENDED("Suspenso"),
        PENDING_VERIFICATION("Pendente de verificação");

        private final String description;

        UserStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDomain that = (UserDomain) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "UserDomain{" +
                "id=" + id +
                ", fullName='" + (fullName != null ? fullName.getFullName() : "null") + '\'' +
                ", email='" + (email != null ? email.getValue() : "null") + '\'' +
                ", age=" + getAge() +
                ", status=" + status +
                '}';
    }
}
