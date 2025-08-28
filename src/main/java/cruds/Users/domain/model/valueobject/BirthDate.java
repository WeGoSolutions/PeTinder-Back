package cruds.Users.domain.model.valueobject;

import cruds.common.exception.NotAllowedException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

/**
 * Value Object para Data de Nascimento seguindo princípios de DDD.
 * Encapsula validações e comportamentos relacionados à idade.
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BirthDate {

    private LocalDate date;

    public BirthDate(LocalDate birthDate) {
        validateBirthDate(birthDate);
        this.date = birthDate;
    }

    /**
     * Valida a data de nascimento
     */
    private void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new NotAllowedException("Data de nascimento é obrigatória");
        }
        
        if (birthDate.isAfter(LocalDate.now())) {
            throw new NotAllowedException("Data de nascimento deve ser no passado");
        }
        
        if (getAge(birthDate) > 120) {
            throw new NotAllowedException("Idade não pode ser superior a 120 anos");
        }
    }

    /**
     * Calcula a idade atual
     */
    public int getAge() {
        return getAge(this.date);
    }

    /**
     * Calcula idade para uma data específica
     */
    private int getAge(LocalDate birthDate) {
        LocalDate hoje = LocalDate.now();
        Period periodo = Period.between(birthDate, hoje);
        return periodo.getYears();
    }

    /**
     * Verifica se é maior de idade (18 anos)
     */
    public boolean isAdult() {
        return getAge() >= 18;
    }

    /**
     * Verifica se é maior de 21 anos (regra específica do sistema)
     */
    public boolean isEligibleForSystem() {
        return getAge() >= 21;
    }

    /**
     * Verifica se é menor de idade
     */
    public boolean isMinor() {
        return getAge() < 18;
    }

    /**
     * Verifica se está em uma faixa etária específica
     */
    public boolean isInAgeRange(int minAge, int maxAge) {
        int currentAge = getAge();
        return currentAge >= minAge && currentAge <= maxAge;
    }

    /**
     * Retorna a faixa etária categórica
     */
    public AgeCategory getAgeCategory() {
        int age = getAge();
        
        if (age < 18) return AgeCategory.MINOR;
        if (age < 25) return AgeCategory.YOUNG_ADULT;
        if (age < 35) return AgeCategory.ADULT;
        if (age < 50) return AgeCategory.MIDDLE_AGED;
        if (age < 65) return AgeCategory.MATURE;
        return AgeCategory.SENIOR;
    }

    /**
     * Enum para categorias de idade
     */
    public enum AgeCategory {
        MINOR("Menor de idade"),
        YOUNG_ADULT("Adulto jovem"),
        ADULT("Adulto"),
        MIDDLE_AGED("Meia idade"),
        MATURE("Maduro"),
        SENIOR("Idoso");

        private final String description;

        AgeCategory(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @Override
    public String toString() {
        return date.toString();
    }
}
