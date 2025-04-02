package cruds.Pets.controller;

import cruds.Pets.exceptions.PetNotFoundException;
import cruds.Pets.exceptions.PetVazioException;
import cruds.Pets.service.PetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import cruds.Pets.entity.Pet;
import cruds.Pets.controller.dto.PetRequestDTO;
import cruds.Pets.controller.dto.PetResponseDTO;
import org.xbill.DNS.dnssec.R;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping
    public ResponseEntity<PetResponseDTO> cadastrarPet(@Valid @RequestBody PetRequestDTO dto) {
        var petCadastrado = petService.cadastrarPet(dto);

        return ResponseEntity.status(201).body(PetResponseDTO.toResponse(petCadastrado));
    }

    @GetMapping("/{id}/imagens")
    public ResponseEntity<List<String>> listarUrlsImagens(HttpServletRequest request, @PathVariable Integer id) {
        var urls = petService.listarUrlsImagens(request, id);
        return ResponseEntity.status(200).body(urls);
    }

    @GetMapping("/{id}/imagens/{indice}")
    public ResponseEntity<byte[]> getImagemPorIndice(@PathVariable Integer id, @PathVariable int indice) {
        var imagem = petService.getImagemPorIndice(id, indice);
        return ResponseEntity.status(200).body(imagem);
    }

    @GetMapping
    public ResponseEntity<List<PetResponseDTO>> listarGeral(){
       var pets = petService.listarGeral();

        return ResponseEntity.status(200).body(pets);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDTO> atualizar(@PathVariable Integer id, @RequestBody PetRequestDTO dto){
            var petAlterado = petService.atualizar(id, dto);

            return ResponseEntity.status(202).body(PetResponseDTO.toResponse(petAlterado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPet(@PathVariable Integer id){
        petService.deletarPet(id);
        return ResponseEntity.status(204).build();
    }
}