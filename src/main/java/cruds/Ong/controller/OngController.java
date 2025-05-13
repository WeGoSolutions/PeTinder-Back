package cruds.Ong.controller;

import cruds.Ong.controller.dto.request.OngRequestCriarDTO;
import cruds.Ong.controller.dto.request.OngRequestImagemDTO;
import cruds.Ong.controller.dto.request.OngRequestLoginDTO;
import cruds.Ong.controller.dto.request.OngRequestUpdateDTO;
import cruds.Ong.controller.dto.response.OngResponseDTO;
import cruds.Ong.controller.dto.response.OngResponseLoginDTO;
import cruds.Ong.entity.Ong;
import cruds.Ong.service.OngService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ong")
@Tag(name = "Ong", description = "Endpoints relacionados a Ongs.")
public class OngController {

    @Autowired
    private OngService ongService;

    @PostMapping
    public ResponseEntity<OngResponseDTO> criarOng(@Valid @RequestBody OngRequestCriarDTO ong) {
        var ongCriada = ongService.criarOng(ong);
        return ResponseEntity.status(201).body(OngResponseDTO.toResponse(ongCriada));
    }

    @PostMapping("/login")
    public ResponseEntity<OngResponseLoginDTO> login(@RequestBody @Valid OngRequestLoginDTO ong) {
        OngResponseLoginDTO response = ongService.login(ong.getEmail(), ong.getSenha());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OngResponseDTO> getOng(@PathVariable Integer id) {
        OngResponseDTO response = ongService.getOng(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OngResponseDTO> updateOng(@PathVariable Integer id, @RequestBody OngRequestUpdateDTO ong) {
        OngResponseDTO response = ongService.updateOng(id, ong);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OngResponseDTO> updateImageOng(@PathVariable Integer id, @RequestBody OngRequestImagemDTO ong) {
        Ong response = ongService.updateImageOng(id, ong.getNomeArquivo(), ong.getImagensBytes());
        return ResponseEntity.ok(OngResponseDTO.toResponse(response));
    }

}
