package cruds.Users.init;

import cruds.Users.application.service.UserManagementService;
import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.time.LocalDate;

/**
 * Inicializador responsável por criar usuários padrão no sistema.
 * Atualizado para usar a nova arquitetura com UserManagementService.
 */
@Component
public class UserInitializer {

    private final UserManagementService userManagementService;

    public UserInitializer(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createDefaultUser() {
        UserRequestCriarDTO user = new UserRequestCriarDTO();
        user.setNome("Super User");
        user.setEmail("usuario@super.com");
        user.setSenha("SenhaForte@123");
        user.setDataNasc(LocalDate.of(1990, 1, 1));
        user.setUserNovo(false);

        try {
            userManagementService.createUser(user);
        } catch (Exception e) {
            System.out.println("Usuário padrão já existe ou erro na criação: " + e.getMessage());
        }
    }
}