package cruds.Users.domain.model.valueobject;

import cruds.common.exception.ConflictException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value Object para Nome completo seguindo princípios de DDD.
 * Encapsula validações e comportamentos relacionados ao nome.
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FullName {

    private String firstName;
    private String lastName;

    public FullName(String fullName) {
        validateAndSplit(fullName);
    }

    public FullName(String firstName, String lastName) {
        validateName(firstName, "Primeiro nome");
        validateName(lastName, "Último nome");
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
    }

    /**
     * Valida e divide o nome completo
     */
    private void validateAndSplit(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new ConflictException("Nome é obrigatório");
        }
        
        fullName = fullName.trim();
        
        if (fullName.length() < 3) {
            throw new ConflictException("Nome deve ter pelo menos 3 caracteres");
        }
        
        if (!fullName.matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$")) {
            throw new ConflictException("Nome deve conter apenas letras e espaços");
        }
        
        String[] parts = fullName.split("\\s+");
        
        if (parts.length < 2) {
            this.firstName = parts[0];
            this.lastName = "";
        } else {
            this.firstName = parts[0];
            this.lastName = String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length));
        }
    }

    /**
     * Valida parte do nome
     */
    private void validateName(String name, String field) {
        if (name == null || name.trim().isEmpty()) {
            throw new ConflictException(field + " é obrigatório");
        }
        
        if (!name.trim().matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$")) {
            throw new ConflictException(field + " deve conter apenas letras e espaços");
        }
    }

    /**
     * Retorna o nome completo
     */
    public String getFullName() {
        if (lastName == null || lastName.isEmpty()) {
            return firstName;
        }
        return firstName + " " + lastName;
    }

    /**
     * Retorna as iniciais do nome
     */
    public String getInitials() {
        StringBuilder initials = new StringBuilder();
        
        if (firstName != null && !firstName.isEmpty()) {
            initials.append(firstName.charAt(0));
        }
        
        if (lastName != null && !lastName.isEmpty()) {
            String[] lastNames = lastName.split("\\s+");
            for (String name : lastNames) {
                if (!name.isEmpty()) {
                    initials.append(name.charAt(0));
                }
            }
        }
        
        return initials.toString().toUpperCase();
    }

    /**
     * Retorna nome formatado para exibição formal
     */
    public String getFormalName() {
        return getFullName();
    }

    @Override
    public String toString() {
        return getFullName();
    }
}
