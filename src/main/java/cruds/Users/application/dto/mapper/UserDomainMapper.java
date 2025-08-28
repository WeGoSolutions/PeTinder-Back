package cruds.Users.application.dto.mapper;

import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import cruds.Users.controller.dto.request.UserRequestUpdateDTO;
import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import cruds.Users.domain.model.UserDomain;
import cruds.Users.entity.User;
import org.springframework.stereotype.Component;

/**
 * Mapper orientado a objetos para conversões entre DTOs e Domain Objects.
 * Segue princípios de POO e encapsula a lógica de transformação.
 */
@Component
public class UserDomainMapper {

    /**
     * Converte DTO de criação para parâmetros de domain
     */
    public UserCreationData fromCreationDto(UserRequestCriarDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return UserCreationData.builder()
            .name(dto.getNome())
            .email(dto.getEmail())
            .password(dto.getSenha())
            .birthDate(dto.getDataNasc())
            .isNewUser(dto.getUserNovo())
            .build();
    }

    /**
     * Converte DTO de atualização para parâmetros de domain
     */
    public UserUpdateData fromUpdateDto(UserRequestUpdateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return UserUpdateData.builder()
            .name(dto.getNome())
            .email(dto.getEmail())
            .birthDate(dto.getDataNasc())
            .cpf(dto.getCpf())
            .cep(dto.getCep())
            .rua(dto.getRua())
            .numero(dto.getNumero())
            .complemento(dto.getComplemento())
            .cidade(dto.getCidade())
            .uf(dto.getUf())
            .build();
    }

    /**
     * Converte UserDomain para DTO de resposta
     */
    public UserResponseCadastroDTO toResponseDto(UserDomain domain) {
        if (domain == null) {
            return null;
        }
        
        UserResponseCadastroDTO.UserResponseCadastroDTOBuilder builder = UserResponseCadastroDTO.builder()
            .id(domain.getId())
            .nome(domain.getFullNameAsString())
            .email(domain.getEmailAsString())
            .dataNasc(domain.getBirthDate() != null ? domain.getBirthDate().getDate() : null)
            .cpf(domain.getCpfUnformatted());

        // Adicionar informações de endereço se disponível
        if (domain.getAddress() != null) {
            builder.cep(domain.getAddress().getCep())
                   .rua(domain.getAddress().getRua())
                   .numero(domain.getAddress().getNumero())
                   .complemento(domain.getAddress().getComplemento())
                   .cidade(domain.getAddress().getCidade())
                   .uf(domain.getAddress().getUf());
        }

        return builder.build();
    }

    /**
     * Converte entidade JPA para DTO de resposta (fallback)
     */
    public UserResponseCadastroDTO toResponseDto(User user) {
        if (user == null) {
            return null;
        }
        
        UserResponseCadastroDTO.UserResponseCadastroDTOBuilder builder = UserResponseCadastroDTO.builder()
            .id(user.getId())
            .nome(user.getNome())
            .email(user.getEmail())
            .dataNasc(user.getDataNasc())
            .cpf(user.getCpf());

        // Adicionar informações de endereço se disponível
        if (user.getEndereco() != null) {
            builder.cep(user.getEndereco().getCep())
                   .rua(user.getEndereco().getRua())
                   .numero(user.getEndereco().getNumero())
                   .complemento(user.getEndereco().getComplemento())
                   .cidade(user.getEndereco().getCidade())
                   .uf(user.getEndereco().getUf());
        }

        return builder.build();
    }

    /**
     * Classes internas para encapsular dados de transferência
     */
    @lombok.Builder
    @lombok.Data
    public static class UserCreationData {
        private String name;
        private String email;
        private String password;
        private java.time.LocalDate birthDate;
        private Boolean isNewUser;
    }

    @lombok.Builder
    @lombok.Data
    public static class UserUpdateData {
        private String name;
        private String email;
        private java.time.LocalDate birthDate;
        private String cpf;
        private String cep;
        private String rua;
        private Integer numero;
        private String complemento;
        private String cidade;
        private String uf;
    }
}
