package cruds.Pets.V2.infrastructure.persistence;

import cruds.Pets.V2.core.adapter.ImagemPetGateway;
import cruds.Pets.V2.core.domain.ImagemPet;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetEntity;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetJpaRepository;
import cruds.Pets.V2.infrastructure.persistence.jpa.mapper.ImagemPetMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ImagemPetGatewayImpl implements ImagemPetGateway {

    private final ImagemPetJpaRepository repository;

    public ImagemPetGatewayImpl(ImagemPetJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public ImagemPet salvar(ImagemPet imagem, UUID petId) {
        ImagemPetEntity entity = ImagemPetMapper.toEntity(imagem, petId);
        ImagemPetEntity savedEntity = repository.save(entity);
        return ImagemPetMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public List<ImagemPet> salvarTodas(List<ImagemPet> imagens, UUID petId) {
        List<ImagemPetEntity> entities = imagens.stream()
                .map(imagem -> ImagemPetMapper.toEntity(imagem, petId))
                .collect(Collectors.toList());
        
        List<ImagemPetEntity> savedEntities = repository.saveAll(entities);
        
        return savedEntities.stream()
                .map(ImagemPetMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ImagemPet> buscarPorPetId(UUID petId) {
        List<ImagemPetEntity> entities = repository.findByPetIdOrderById(petId);
        return entities.stream()
                .map(ImagemPetMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void remover(UUID imagemId) {
        repository.deleteById(imagemId);
    }

    @Override
    @Transactional
    public void removerPorPetId(UUID petId) {
        repository.deleteByPetId(petId);
    }

    @Override
    public ImagemPet buscarPorIndice(UUID petId, int indice) {
        List<ImagemPetEntity> entities = repository.findByPetIdOrderById(petId);
        if (indice >= 0 && indice < entities.size()) {
            return ImagemPetMapper.toDomain(entities.get(indice));
        }
        return null;
    }
}