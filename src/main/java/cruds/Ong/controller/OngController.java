package cruds.Ong.controller;

import cruds.Imagem.service.ImagemOngService;
import cruds.Ong.controller.dto.request.*;
import cruds.Ong.controller.dto.response.*;
import cruds.Ong.entity.Ong;
import cruds.Ong.service.OngService;
import cruds.Users.controller.dto.response.UserResponseUrlDTO;
import cruds.Users.service.UserService;
import cruds.common.exception.BadRequestException;
import cruds.common.dto.ImageUploadData;
import cruds.common.util.ImageUploadUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ongs")
@Tag(name = "Ong", description = "Endpoints relacionados a Ongs.")
public class OngController {

    @Autowired
    private OngService ongService;

    @Autowired
    private ImagemOngService imagemOngService;
    @Autowired
    private UserService userService;

    @Operation(summary = "Criar ONG")
    @PostMapping
    public ResponseEntity<OngResponseDTO> criarOng(@Valid @RequestBody OngRequestCriarDTO ong) {
        var ongCriada = ongService.criarOng(ong);
        return ResponseEntity.status(201).body(OngResponseDTO.toResponse(ongCriada));
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<OngResponseDTO> updatePassword(@PathVariable Integer id,
                                                         @Valid @RequestBody OngRequestUpdatePasswordDTO req) {
        OngResponseDTO response = ongService.updatePassword(id, req.getSenhaAtual(), req.getNovaSenha());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Realiza login da ONG")
    @PostMapping("/login")
    public ResponseEntity<OngResponseLoginDTO> login(@RequestBody @Valid OngRequestLoginDTO ong) {
        OngResponseLoginDTO response = ongService.login(ong.getEmail(), ong.getSenha());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retorna a ong pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<OngResponseDTO> getOng(@PathVariable Integer id) {
        OngResponseDTO response = ongService.getOng(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualiza a ONG")
    @PatchMapping("/{id}")
    public ResponseEntity<OngResponseDTO> updateOng(@PathVariable Integer id, @Valid @RequestBody OngRequestUpdateDTO ong) {
        OngResponseDTO response = ongService.updateOng(id, ong);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualiza a imagem da ONG")
    @PutMapping(value = "/{id}/imagem")
    public ResponseEntity<OngResponseUrlDTO> updateImageOng(@PathVariable Integer id,
                                                         @RequestBody @Valid OngRequestImagemDTO imagem) {
        OngResponseUrlDTO updatedUser = ongService.updateImagem(id, imagem);
        return ResponseEntity.status(200).body(updatedUser);
    }

    @Operation(summary = "Exibe a imagem da ONG")
    @GetMapping("/{id}/imagem/arquivo")
    public ResponseEntity<OngResponseUrlDTO> getOngImage(@PathVariable Integer id) {
        OngResponseUrlDTO response = ongService.getImageOng(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista todos os pets da ONG")
    @GetMapping("/{id}/pets")
    public ResponseEntity<List<OngResponsePetsDTO>> listarPets(@PathVariable Integer id) {
        var pets = ongService.listarTodosPetsDeOng(id);
        if (pets.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(pets);
    }

    @Operation(summary = "Lista mensagens pendentes para a ONG")
    @GetMapping("/{id}/mensagens-pendentes")
    public ResponseEntity<List<OngResponseMensagensPendingDTO>> listarMensagensPendentes(
            @PathVariable Integer id,
            HttpServletRequest request) {
        List<OngResponseMensagensPendingDTO> mensagens = ongService.listarMensagensPendentes(id, request);
        return ResponseEntity.ok(mensagens);
    }


}