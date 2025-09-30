package cruds.Ong.V2.infrastructure.config;

import cruds.Ong.V2.core.adapter.ArmazenamentoImagemGateway;
import cruds.Ong.V2.core.adapter.CriptografiaGateway;
import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OngConfig {

    @Bean
    public CriarOngUseCase criarOngUseCase(OngGateway ongGateway, CriptografiaGateway criptografiaGateway) {
        return new CriarOngUseCase(ongGateway, criptografiaGateway);
    }

    @Bean
    public BuscarOngPorIdUseCase buscarOngPorIdUseCase(OngGateway ongGateway) {
        return new BuscarOngPorIdUseCase(ongGateway);
    }

    @Bean
    public ListarOngsUseCase listarOngsUseCase(OngGateway ongGateway) {
        return new ListarOngsUseCase(ongGateway);
    }

    @Bean
    public AtualizarOngUseCase atualizarOngUseCase(OngGateway ongGateway) {
        return new AtualizarOngUseCase(ongGateway);
    }

    @Bean
    public AtualizarSenhaOngUseCase atualizarSenhaOngUseCase(OngGateway ongGateway, CriptografiaGateway criptografiaGateway) {
        return new AtualizarSenhaOngUseCase(ongGateway, criptografiaGateway);
    }

    @Bean
    public RemoverOngUseCase removerOngUseCase(OngGateway ongGateway) {
        return new RemoverOngUseCase(ongGateway);
    }

    @Bean
    public LoginOngUseCase loginOngUseCase(OngGateway ongGateway, CriptografiaGateway criptografiaGateway) {
        return new LoginOngUseCase(ongGateway, criptografiaGateway);
    }

    @Bean
    public UploadImagemOngUseCase uploadImagemOngUseCase(
            OngGateway ongGateway,
            ArmazenamentoImagemGateway armazenamentoImagemGateway) {
        return new UploadImagemOngUseCase(ongGateway, armazenamentoImagemGateway);
    }

    @Bean
    public BuscarImagemOngUseCase buscarImagemOngUseCase(OngGateway ongGateway) {
        return new BuscarImagemOngUseCase(ongGateway);
    }
}
