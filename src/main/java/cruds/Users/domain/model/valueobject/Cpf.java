package cruds.Users.domain.model.valueobject;

import cruds.common.exception.ConflictException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value Object para CPF seguindo princípios de DDD.
 * Encapsula validações e comportamentos relacionados ao CPF.
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cpf {

    private String number;

    public Cpf(String cpf) {
        validateAndFormat(cpf);
    }

    /**
     * Valida e formata o CPF
     */
    private void validateAndFormat(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new ConflictException("CPF é obrigatório");
        }
        
        // Remove formatação
        String cleanCpf = cpf.replaceAll("[^0-9]", "");
        
        if (cleanCpf.length() != 11) {
            throw new ConflictException("CPF deve ter 11 dígitos");
        }
        
        if (!isValidCpf(cleanCpf)) {
            throw new ConflictException("CPF inválido");
        }
        
        this.number = cleanCpf;
    }

    /**
     * Valida CPF usando algoritmo oficial
     */
    private boolean isValidCpf(String cpf) {
        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        try {
            // Calcula primeiro dígito verificador
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int firstDigit = 11 - (sum % 11);
            if (firstDigit > 9) firstDigit = 0;
            
            // Calcula segundo dígito verificador
            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int secondDigit = 11 - (sum % 11);
            if (secondDigit > 9) secondDigit = 0;
            
            // Verifica se os dígitos calculados conferem
            return firstDigit == Character.getNumericValue(cpf.charAt(9)) &&
                   secondDigit == Character.getNumericValue(cpf.charAt(10));
                   
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Retorna CPF formatado (XXX.XXX.XXX-XX)
     */
    public String getFormatted() {
        if (number == null || number.length() != 11) {
            return number;
        }
        
        return String.format("%s.%s.%s-%s",
            number.substring(0, 3),
            number.substring(3, 6),
            number.substring(6, 9),
            number.substring(9, 11)
        );
    }

    /**
     * Retorna CPF sem formatação
     */
    public String getUnformatted() {
        return number;
    }

    @Override
    public String toString() {
        return getFormatted();
    }
}
