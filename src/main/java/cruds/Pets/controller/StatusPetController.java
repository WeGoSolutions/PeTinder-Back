package cruds.Pets.controller;

import cruds.Pets.entity.StatusPet;
import cruds.Pets.repository.StatusPetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
public class StatusPetController {

    private final StatusPetRepository statusPetRepository;

    @GetMapping
    public ResponseEntity<List<StatusPet>> listar() {
        List<StatusPet> statusPets = statusPetRepository.findAll();

        if (statusPets.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(statusPets);
    }

    @GetMapping("/liked")
    public ResponseEntity<List<StatusPet>> curtidos() {
        List<StatusPet> statusPetsCurtidos = statusPetRepository.findAllLikedStatusPets();

        if (statusPetsCurtidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(statusPetsCurtidos);
    }

    @GetMapping("/liked/{usuarioId}")
    public ResponseEntity<List<StatusPet>> curtidosPorUsuario(@PathVariable Integer usuarioId) {
        List<StatusPet> statusPetsCurtidos = statusPetRepository.findLikedStatusPetsByUsuarioId(usuarioId);

        if (statusPetsCurtidos.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(statusPetsCurtidos);
    }

}
