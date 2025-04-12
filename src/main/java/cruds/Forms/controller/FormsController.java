package cruds.Forms.controller;

import cruds.Forms.controller.dto.request.FormRequestCriarDTO;
import cruds.Forms.entity.Forms;
import cruds.Forms.controller.dto.response.FormResponsePreenchimentoUserDTO;
import cruds.Forms.service.FormsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/forms")
@Validated
public class FormsController {

    @Autowired
    FormsService formsService;

    @GetMapping("/{id}/dados-formulario")
    public ResponseEntity<FormResponsePreenchimentoUserDTO> getDadosFormulario(@PathVariable Integer id) {
        FormResponsePreenchimentoUserDTO dados = formsService.getDadosFormulario(id);
        return ResponseEntity.ok(dados);
    }

    @PostMapping
    public ResponseEntity<Forms> createForm(@RequestBody @Valid FormRequestCriarDTO form) {
        Forms createdForm = formsService.createForm(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdForm);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Forms> updateForm(@PathVariable Integer id, @RequestBody @Valid FormRequestCriarDTO form) {
        Forms updatedForm = formsService.updateForm(id, form);
        return ResponseEntity.ok(updatedForm);
    }
}
