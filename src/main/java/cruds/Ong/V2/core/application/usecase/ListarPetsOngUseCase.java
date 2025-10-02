package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.PetOngGateway;

import java.util.List;
import java.util.UUID;

public class ListarPetsOngUseCase {

    private final PetOngGateway petOngGateway;

    public ListarPetsOngUseCase(PetOngGateway petOngGateway) {
        this.petOngGateway = petOngGateway;
    }

    public List<PetOngGateway.PetOngInfo> listarPets(UUID ongId) {
        return petOngGateway.listarPetsPorOng(ongId);
    }
}

