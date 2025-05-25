package cruds.Ong.controller;

import cruds.Imagem.entity.ImagemOng;
import cruds.Imagem.service.ImagemOngService;
import cruds.Ong.controller.dto.request.OngRequestCriarDTO;
import cruds.Ong.controller.dto.request.OngRequestImagemDTO;
import cruds.Ong.controller.dto.request.OngRequestLoginDTO;
import cruds.Ong.controller.dto.request.OngRequestUpdateDTO;
import cruds.Ong.controller.dto.response.OngResponseDTO;
import cruds.Ong.controller.dto.response.OngResponseLoginDTO;
import cruds.Ong.controller.dto.response.OngResponseUrlDTO;
import cruds.Ong.entity.Ong;
import cruds.Ong.service.OngService;
import cruds.common.exception.BadRequestException;
import cruds.common.dto.ImageUploadData;
import cruds.common.util.ImageUploadUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;

@RestController
@RequestMapping("/ongs")
@Tag(name = "Ong", description = "Endpoints relacionados a Ongs.")
public class OngController {

    @Autowired
    private OngService ongService;

    @Autowired
    private ImagemOngService imagemOngService;

    @Operation(summary = "Criar ONG")
    @PostMapping
    public ResponseEntity<OngResponseDTO> criarOng(@Valid @RequestBody OngRequestCriarDTO ong) {
        var ongCriada = ongService.criarOng(ong);
        return ResponseEntity.status(201).body(OngResponseDTO.toResponse(ongCriada));
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
    public ResponseEntity<OngResponseDTO> updateOng(@PathVariable Integer id, @RequestBody OngRequestUpdateDTO ong) {
        OngResponseDTO response = ongService.updateOng(id, ong);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Sobe a imagem da ONG")
    @PostMapping(value = "/{id}/imagem")
    public ResponseEntity<OngResponseDTO> updateImageOng(@PathVariable Integer id,
                                                         @RequestBody @Valid OngRequestImagemDTO imagem) {
        try {
            ImageUploadData uploadData = ImageUploadUtil.parseImageUploadRequest(imagem.getImagensBytes(),
                    imagem.getNomeArquivo());

            Ong ongAtualizada = ongService.uploadOngImage(id, uploadData.getImageBytes(), uploadData.getNomeArquivo(),
                    uploadData.getExtension());
            return ResponseEntity.ok(OngResponseDTO.toResponse(ongAtualizada));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "Retorna a imagem da ONG")
    @GetMapping("/{id}/imagem/arquivo")
    public ResponseEntity<byte[]> getOngImage(@PathVariable Integer id) {
        byte[] imageData = ongService.getOngImageBytes(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }
}