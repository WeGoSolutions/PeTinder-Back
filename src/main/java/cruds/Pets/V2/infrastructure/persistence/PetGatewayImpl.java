package cruds.Pets.V2.infrastructure.persistence;

import cruds.Pets.V2.core.adapter.ImagemPetGateway;
import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.domain.Pet;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetJpaRepository;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetJpaRepository;
import cruds.Pets.V2.infrastructure.persistence.jpa.mapper.PetMapper;
import cruds.Pets.repository.PetStatusRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PetGatewayImpl implements PetGateway {

    private final PetJpaRepository petJpaRepository;
    private final PetStatusRepository petStatusRepository;
    private final ImagemPetGateway imagemPetGateway;
    private final ImagemPetJpaRepository imagemPetJpaRepository;

    public PetGatewayImpl(
            PetJpaRepository petJpaRepository,
            PetStatusRepository petStatusRepository,
            ImagemPetGateway imagemPetGateway,
            ImagemPetJpaRepository imagemPetJpaRepository
    ) {
        this.petJpaRepository = petJpaRepository;
        this.petStatusRepository = petStatusRepository;
        this.imagemPetGateway = imagemPetGateway;
        this.imagemPetJpaRepository = imagemPetJpaRepository;
    }

    @Override
    public Pet salvar(Pet pet) {
        var entity = PetMapper.toEntity(pet);
        var savedEntity = petJpaRepository.save(entity);
        return PetMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public Pet atualizar(Pet pet) {
        var entity = PetMapper.toEntity(pet);
        var updatedEntity = petJpaRepository.save(entity);
        return PetMapper.toDomain(updatedEntity);
    }

    @Override
    public Optional<Pet> buscarPorId(UUID id) {
        return petJpaRepository.findById(id)
                .map(entity -> {
                    Pet pet = PetMapper.toDomain(entity);
                    List<String> keys = imagemPetJpaRepository.findKeysByPetId(id);

                    if (keys != null && !keys.isEmpty()) {
                        pet.setImagens(imagemPetGateway.buscarPorPetId(id, keys));
                    }

                    return pet;
                });
    }

    @Override
    public List<Pet> listarTodos() {
        return petJpaRepository.findAll()
                .stream()
                .map(entity -> {
                    Pet pet = PetMapper.toDomain(entity);
                    List<String> keys = imagemPetJpaRepository.findKeysByPetId(pet.getId());
                    if (keys != null && !keys.isEmpty()) {
                        pet.setImagens(imagemPetGateway.buscarPorPetId(pet.getId(), keys));
                    }
                    return pet;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Pet> listarPorOng(UUID ongId) {
        return petJpaRepository.findByOngIdOrderByCurtidasDesc(ongId)
                .stream()
                .map(entity -> {
                    Pet pet = PetMapper.toDomain(entity);
                    List<String> keys = imagemPetJpaRepository.findKeysByPetId(pet.getId());
                    if (keys != null && !keys.isEmpty()) {
                        pet.setImagens(imagemPetGateway.buscarPorPetId(pet.getId(), keys));
                    }
                    return pet;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Pet> listarDisponiveis() {
        return petJpaRepository.findByIsAdotadoFalseOrIsAdotadoIsNull()
                .stream()
                .map(entity -> {
                    Pet pet = PetMapper.toDomain(entity);
                    List<String> keys = imagemPetJpaRepository.findKeysByPetId(pet.getId());
                    if (keys != null && !keys.isEmpty()) {
                        pet.setImagens(imagemPetGateway.buscarPorPetId(pet.getId(), keys));
                    }
                    return pet;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Pet> listarPetsNaoInteragidosPorUsuario(UUID userId) {
        return petStatusRepository.findPetsNotInteractedByUser(userId)
                .stream()
                .map(pet -> PetMapper.toDomain(PetMapper.toV2Entity(pet)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void remover(UUID id) {
        imagemPetJpaRepository.deleteAllByPetId(id);
        petJpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(UUID id) {
        return petJpaRepository.existsById(id);
    }

    @Override
    public void removerTodos() {
        petJpaRepository.deleteAll();
    }
}
