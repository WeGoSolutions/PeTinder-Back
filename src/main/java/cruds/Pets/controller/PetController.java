package cruds.Pets.controller;

import cruds.Pets.controller.dto.request.PetRequestCriarDTO;
import cruds.Pets.controller.dto.request.PetRequestCurtirDTO;
import cruds.Pets.controller.dto.request.UploadImagesRequest;
import cruds.Pets.controller.dto.response.PetResponseCriarDTO;
import cruds.Pets.controller.dto.response.PetResponseCurtirDTO;
import cruds.Pets.controller.dto.response.PetResponseGeralDTO;
import cruds.Pets.entity.Pet;
import cruds.Pets.service.PetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
@Tag(name = "Pet", description = "Endpoints relacionados ao gerenciamento de pets.")
public class PetController {

    @Autowired
    private PetService petService;

    @Operation(summary = "Cadastra um novo pet")
    @PostMapping
    public ResponseEntity<PetResponseCriarDTO> cadastrarPet(@Valid @RequestBody PetRequestCriarDTO dto) {
        var petCadastrado = petService.cadastrarPet(dto);
        return ResponseEntity.status(201).body(PetResponseCriarDTO.toResponse(petCadastrado));
    }

    @Operation(summary = "Faz upload das imagens do pet")
    @PostMapping("/{id}/upload-imagens")
    public ResponseEntity<PetResponseCriarDTO> uploadPetImages(@PathVariable Integer id,
                                                               @RequestBody UploadImagesRequest request) {
        var petAtualizado = petService.uploadPetImages(id, request.getImagensBytes(), request.getNomesArquivos());
        return ResponseEntity.status(200).body(PetResponseCriarDTO.toResponse(petAtualizado));
    }

    @Operation(summary = "Lista URLs das imagens do pet")
    @GetMapping("/{id}/imagens")
    public ResponseEntity<List<String>> listarUrlsImagens(HttpServletRequest request,
                                                          @PathVariable Integer id) {
        var urls = petService.listarUrlsImagens(request, id);
        return ResponseEntity.status(200).body(urls);
    }

    @Operation(summary = "Lista todos os pets")
    @GetMapping
    public ResponseEntity<List<PetResponseGeralDTO>> listarGeral() {
        var pets = petService.listarGeral();
        return ResponseEntity.status(200).body(pets);
    }

    @Operation(summary = "Exibe a imagem especifica do pet")
    @GetMapping("/{id}/imagens/{indice}")
    public ResponseEntity<byte[]> getImagemPorIndice(@PathVariable Integer id,
                                                     @PathVariable int indice) {
        byte[] imagem = petService.getImagemPorIndice(id, indice);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imagem);
    }

    @Operation(summary = "Atualiza os dados de um pet")
    @PutMapping("/{id}")
    public ResponseEntity<PetResponseCriarDTO> atualizar(@PathVariable Integer id,
                                                         @RequestBody PetRequestCriarDTO dto) {
        var petAlterado = petService.atualizar(id, dto);
        return ResponseEntity.status(200).body(PetResponseCriarDTO.toResponse(petAlterado));
    }

    @Operation(summary = "Deleta um pet")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        petService.deletarPet(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Curtir um pet")
    @PutMapping("/curtir/{id}")
    public ResponseEntity<PetResponseCurtirDTO> curtirPet(@PathVariable Integer id, @RequestBody PetRequestCurtirDTO dto) {
        var petAlterado = petService.curtirPet(id, dto);
        return ResponseEntity.status(202).body(PetResponseCurtirDTO.toResponse(petAlterado));
    }

    @Operation(summary = "Apagar uma imagem do pet")
    @DeleteMapping("/{id}/imagens/{indice}")
    public ResponseEntity<Void> apagarImagem(@PathVariable Integer id,
                                             @PathVariable int indice) {
        petService.apagarImagem(id, indice);
        return ResponseEntity.status(204).build();
    }

}