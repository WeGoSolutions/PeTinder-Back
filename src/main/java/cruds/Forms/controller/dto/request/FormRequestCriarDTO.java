package cruds.Forms.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FormRequestCriarDTO {
    @NotNull
    private UUID petId;
    @NotNull
    private UUID userId;

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @NotBlank
    private String email;

    @NotNull
    private LocalDate dataNasc;

    @NotBlank
    private String telefone;

    @NotBlank
    private String cep;

    private String complemento;

    @NotBlank
    private String rua;

    @NotNull
    private Integer numero;

    @NotBlank
    private String cidade;

    @NotBlank
    private String uf;

    @NotBlank
    private String tipoMoradia;

    private String aluguePodeAnimal;

    private String infosCasa;

    @NotBlank
    private String possuiPet;

    private List<String> castradoOrVacinado;

    private String infosPet;

    @NotNull
    @Size(min = 5, message = "Deve haver no mínimo 5 imagens")
    private List<MultipartFile> imagens;
}