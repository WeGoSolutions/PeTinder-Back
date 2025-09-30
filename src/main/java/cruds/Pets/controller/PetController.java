package cruds.Pets.controller;

import cruds.Pets.controller.dto.request.PetRequestCriarDTO;
import cruds.Pets.controller.dto.request.UploadImagesRequest;
import cruds.Pets.controller.dto.response.PetResponseCriarDTO;
import cruds.Pets.controller.dto.response.PetResponseGeralDTO;
import cruds.Pets.entity.Pet;
import cruds.Pets.repository.PetRepository;
import cruds.Pets.service.PetService;
import cruds.common.exception.NotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pets")
@Tag(name = "Pet", description = "Endpoints relacionados ao gerenciamento de pets.")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private PetRepository petRepository;

    @Operation(summary = "Cadastra um novo pet")
    @PostMapping
    public ResponseEntity<PetResponseCriarDTO> cadastrarPet(@Valid @RequestBody PetRequestCriarDTO dto) {
        var petCadastrado = petService.cadastrarPet(dto);
        return ResponseEntity.status(201).body(PetResponseCriarDTO.toResponse(petCadastrado));
    }

    @Operation(summary = "Faz upload das imagens do pet")
    @PostMapping("/{id}/upload-imagens")
    public ResponseEntity<PetResponseCriarDTO> uploadPetImages(@PathVariable UUID id,
                                                               @RequestBody UploadImagesRequest request) {
        var petAtualizado = petService.uploadPetImages(id, request.getImagensBytes(), request.getNomesArquivos());
        return ResponseEntity.status(200).body(PetResponseCriarDTO.toResponse(petAtualizado));
    }

    @Operation(summary = "Lista URLs das imagens do pet")
    @GetMapping("/{id}/imagens")
    public ResponseEntity<List<String>> listarUrlsImagens(HttpServletRequest request,
                                                          @PathVariable UUID id) {
        var urls = petService.listarUrlsImagens(request, id);
        return ResponseEntity.status(200).body(urls);
    }

    @Operation(summary = "Lista todos os pets com paginação")
    @GetMapping
    public ResponseEntity<List<PetResponseGeralDTO>> listarGeral(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<PetResponseGeralDTO> pets = petService.listarGeral(page, size);
        long totalPets = petService.contarTotalPets();
        int totalPages = (int) Math.ceil((double) totalPets / size);

        return ResponseEntity.status(200)
                .header("Total-Pets", String.valueOf(totalPets))
                .header("Total-Paginas", String.valueOf(totalPages))
                .header("Pagina-Atual", String.valueOf(page))
                .header("Tamanho-Pagina", String.valueOf(size))
                .body(pets);
    }

    @Operation(summary = "Exibe a imagem especifica do pet")
    @GetMapping("/{id}/imagens/{indice}")
    public ResponseEntity<byte[]> getImagemPorIndice(@PathVariable UUID id,
                                                     @PathVariable int indice) {
        byte[] imagem = petService.getImagemPorIndice(id, indice);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imagem);
    }

    @Operation(summary = "Atualiza os dados de um pet")
    @PutMapping("/{id}")
    public ResponseEntity<PetResponseCriarDTO> atualizar(@PathVariable UUID id,
                                                         @RequestBody PetRequestCriarDTO dto) {
        var petAlterado = petService.atualizar(id, dto);
        return ResponseEntity.status(200).body(PetResponseCriarDTO.toResponse(petAlterado));
    }

    @Operation(summary = "Deleta um pet")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        petService.deletarPet(id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/{id}/imagem/{index}")
    public ResponseEntity<byte[]> getPetImage(
            @PathVariable UUID id,
            @PathVariable int index) {

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet não encontrado: " + id));

        if (pet.getImagens() == null || pet.getImagens().size() <= index) {
            throw new NotFoundException("Imagem não encontrada no índice: " + index);
        }

        String caminho = pet.getImagens().get(index).getCaminho();
        try {
            byte[] data = Files.readAllBytes(Paths.get(caminho));
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(data);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler imagem: " + e.getMessage());
        }
    }

    @Operation(summary = "Apagar uma imagem do pet")
    @DeleteMapping("/{id}/imagens/{indice}")
    public ResponseEntity<Void> apagarImagem(@PathVariable UUID id,
                                             @PathVariable int indice) {
        petService.apagarImagem(id, indice);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Busca pet por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PetResponseGeralDTO> getPetById(@PathVariable UUID id) {
        var pet = petService.getPetById(id);
        return ResponseEntity.ok(pet);
    }

}