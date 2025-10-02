package cruds.Pets.V2.core.adapter;

import cruds.Pets.V2.core.domain.ImagemPet;

import java.util.List;
import java.util.UUID;

public interface ImagemPetGateway {

    ImagemPet salvar(ImagemPet imagem, UUID petId);
    
    List<ImagemPet> salvarTodas(List<ImagemPet> imagens, UUID petId);

    List<ImagemPet> buscarPorPetId(UUID petId);

    void remover(UUID imagemId);
    
    void removerPorPetId(UUID petId);

    ImagemPet buscarPorIndice(UUID petId, int indice);
}