package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.Pet;
import cruds.Pets.V2.core.domain.PetStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.function.Function;

public class ListarPetsDisponivelParaUsuarioUseCase {

    private final PetGateway petGateway;
    private final UsuarioGateway userGateway;
    private final PetStatusGateway petStatusGateway;

    public ListarPetsDisponivelParaUsuarioUseCase(PetGateway petGateway, UsuarioGateway userGateway, PetStatusGateway petStatusGateway) {
        this.petGateway = petGateway;
        this.userGateway = userGateway;
        this.petStatusGateway = petStatusGateway;
    }

    public List<Pet> listarDisponiveis(UUID userId) {
        if (!userGateway.existePorId(userId)) {
            throw new PetException("Usuário com ID " + userId + " não encontrado");
        }

        return petGateway.listarDisponiveis()
                .stream()
                .filter(Pet::estaDisponivel)
                .collect(Collectors.toList());
    }

    public Map<UUID, PetStatus> buscarStatusDosPets(UUID userId, List<Pet> pets) {
        List<UUID> petIds = pets.stream().map(Pet::getId).toList();

        return petIds.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        petId -> petStatusGateway.buscarPorPetEUsuario(petId, userId).orElse(null),
                        (existing, replacement) -> existing
                ));
    }
}