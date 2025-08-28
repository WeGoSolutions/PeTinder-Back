package cruds.Users.domain.model.valueobject;

import cruds.common.exception.ConflictException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value Object para Email seguindo princípios de DDD.
 * Encapsula validações e comportamentos relacionados ao email.
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

    private String value;

    public Email(String email) {
        validateEmail(email);
        this.value = email.toLowerCase().trim();
    }

    /**
     * Valida se o email tem formato válido
     */
    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ConflictException("Email é obrigatório");
        }
        
        email = email.trim();
        
        if (!email.contains("@")) {
            throw new ConflictException("Email deve ter formato válido");
        }
        
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ConflictException("Email deve ter formato válido");
        }
    }

    /**
     * Verifica se o email é do mesmo domínio
     */
    public boolean isSameDomain(Email other) {
        if (other == null) return false;
        
        String thisDomain = this.value.substring(this.value.indexOf("@") + 1);
        String otherDomain = other.value.substring(other.value.indexOf("@") + 1);
        
        return thisDomain.equals(otherDomain);
    }

    /**
     * Obtém o domínio do email
     */
    public String getDomain() {
        return this.value.substring(this.value.indexOf("@") + 1);
    }

    /**
     * Obtém a parte local do email (antes do @)
     */
    public String getLocalPart() {
        return this.value.substring(0, this.value.indexOf("@"));
    }

    @Override
    public String toString() {
        return value;
    }
}
