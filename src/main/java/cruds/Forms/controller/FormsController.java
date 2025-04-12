// linguagem: java
package cruds.Forms.controller;

import cruds.Forms.controller.dto.request.FormRequestCriarDTO;
import cruds.Forms.entity.Forms;
import cruds.Forms.controller.dto.response.FormResponsePreenchimentoUserDTO;
import cruds.Forms.service.FormsService;
import cruds.Imagem.entity.Imagem;
import cruds.Imagem.repository.ImagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/forms")
@Validated
public class FormsController {

    @Autowired
    FormsService formsService;

    @Autowired
    private ImagemRepository imagemRepository;

    @GetMapping("/{id}/dados-formulario")
    public ResponseEntity<FormResponsePreenchimentoUserDTO> getDadosFormulario(@PathVariable Integer id) {
        FormResponsePreenchimentoUserDTO dados = formsService.getDadosFormulario(id);
        return ResponseEntity.ok(dados);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Forms> createForm(@ModelAttribute @Valid FormRequestCriarDTO form) {
        Forms createdForm = formsService.createForm(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdForm);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Forms> updateForm(@PathVariable Integer id, @ModelAttribute @Valid FormRequestCriarDTO form) {
        Forms updatedForm = formsService.updateForm(id, form);
        return ResponseEntity.ok(updatedForm);
    }

    @GetMapping("/{formId}/imagens")
    public ResponseEntity<List<String>> getImagens(@PathVariable Integer formId) {
        List<Imagem> imagens = imagemRepository.findByFormId(formId);

        if (imagens.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<String> imagensBase64 = imagens.stream()
                .map(img -> "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(img.getDados()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(imagensBase64, HttpStatus.OK);
    }

    @GetMapping("/imagens/{id}")
    public ResponseEntity<byte[]> getImagem(@PathVariable Integer id) {
        return imagemRepository.findById(id)
                .map(imagem -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG);
                    return new ResponseEntity<>(imagem.getDados(), headers, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{formId}/imagens/{indice}")
    public ResponseEntity<byte[]> getImagemPorIndice(@PathVariable Integer formId,
                                                     @PathVariable int indice) {
        Forms form = formsService.obterFormPorId(formId);
        List<Imagem> imagens = form.getImagens();
        if (imagens == null || imagens.isEmpty() || indice < 0 || indice >= imagens.size()) {
            return ResponseEntity.notFound().build();
        }
        byte[] dados = imagens.get(indice).getDados();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(dados, headers, HttpStatus.OK);
    }
}