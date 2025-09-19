package cruds.Pets.V2.infrastructure.config;

import cruds.Pets.V2.core.adapter.OngGateway;
import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Pets.V2.core.application.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PetConfig {

    @Bean
    public CriarPetUseCase criarPetUseCase(PetGateway petGateway, OngGateway ongGateway) {
        return new CriarPetUseCase(petGateway, ongGateway);
    }

    @Bean
    public BuscarPetPorIdUseCase buscarPetPorIdUseCase(PetGateway petGateway) {
        return new BuscarPetPorIdUseCase(petGateway);
    }

    @Bean
    public ListarPetsUseCase listarPetsUseCase(PetGateway petGateway) {
        return new ListarPetsUseCase(petGateway);
    }

    @Bean
    public AtualizarPetUseCase atualizarPetUseCase(PetGateway petGateway) {
        return new AtualizarPetUseCase(petGateway);
    }

    @Bean
    public RemoverPetUseCase removerPetUseCase(PetGateway petGateway) {
        return new RemoverPetUseCase(petGateway);
    }

    @Bean
    public AdotarPetUseCase adotarPetUseCase(PetGateway petGateway) {
        return new AdotarPetUseCase(petGateway);
    }

    @Bean
    public CurtirPetUseCase curtirPetUseCase(PetGateway petGateway) {
        return new CurtirPetUseCase(petGateway);
    }

    @Bean
    public ListarPetsDisponivelParaUsuarioUseCase listarPetsDisponivelParaUsuarioUseCase(
            PetGateway petGateway, UsuarioGateway usuarioGateway) {
        return new ListarPetsDisponivelParaUsuarioUseCase(petGateway, usuarioGateway);
    }
}