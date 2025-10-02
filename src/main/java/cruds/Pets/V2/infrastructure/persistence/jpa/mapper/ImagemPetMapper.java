package cruds.Pets.V2.infrastructure.persistence.jpa.mapper;

import cruds.Pets.V2.core.domain.ImagemPet;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetEntity;

import java.util.UUID;

public class ImagemPetMapper {

    public static ImagemPetEntity toEntity(ImagemPet imagem, UUID petId) {
        if (imagem == null) return null;
        
        return new ImagemPetEntity(
                imagem.getId(),
                imagem.getCaminho(),
                imagem.getNomeArquivo(),
                petId
        );
    }

    public static ImagemPet toDomain(ImagemPetEntity entity) {
        if (entity == null) return null;
        
        return new ImagemPet(
                entity.getId(),
                entity.getCaminho(),
                entity.getNomeArquivo(),
                null // dados não são armazenados no banco, apenas caminho
        );
    }
}