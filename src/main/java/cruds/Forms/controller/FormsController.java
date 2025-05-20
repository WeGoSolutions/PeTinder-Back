package cruds.Forms.controller;

import cruds.Forms.controller.dto.request.FormRequestCriarDTO;
import cruds.Forms.entity.Forms;
import cruds.Forms.controller.dto.response.FormResponsePreenchimentoUserDTO;
import cruds.Forms.service.FormsService;
import cruds.Imagem.entity.Imagem;
import cruds.Imagem.entity.ImagemForms;
import cruds.Imagem.repository.ImagemFormsRepository;
import cruds.Imagem.repository.ImagemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/forms")
@Validated
@Tag(name = "Formulario", description = "Endpoints relacionados a criação do formulario.")
public class FormsController {

    @Autowired
    FormsService formsService;

    @Autowired
    private ImagemFormsRepository imagemRepository;

    @Operation(summary = "Obtém os dados do formulário para preenchimento do usuário")
    @GetMapping("/{id}/dados-formulario")
    public ResponseEntity<FormResponsePreenchimentoUserDTO> getDadosFormulario(@PathVariable Integer id) {
        FormResponsePreenchimentoUserDTO dados = formsService.getDadosFormulario(id);
        return ResponseEntity.ok(dados);
    }

    @Operation(summary = "Cria um novo formulário")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Forms> createForm(@ModelAttribute @Valid FormRequestCriarDTO form) {
        Forms createdForm = formsService.createForm(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdForm);
    }

    @Operation(summary = "Atualiza um formulário")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Forms> updateForm(@PathVariable Integer id, @ModelAttribute @Valid FormRequestCriarDTO form) {
        Forms updatedForm = formsService.updateForm(id, form);
        return ResponseEntity.ok(updatedForm);
    }

    @Operation(summary = "Retorna imagens do formulário no formato Base64")
    @GetMapping("/{formId}/imagens")
    public ResponseEntity<List<String>> getImagens(@PathVariable Integer formId) {
        List<ImagemForms> imagens = imagemRepository.findByFormId(formId);

        if (imagens.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<String> imagensBase64 = imagens.stream()
                .map(img -> {
                    try {
                        byte[] dados = Files.readAllBytes(Paths.get(img.getCaminho()));
                        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(dados);
                    } catch (IOException e) {
                        throw new RuntimeException("Erro ao carregar imagem: " + e.getMessage());
                    }
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(imagensBase64, HttpStatus.OK);
    }
}