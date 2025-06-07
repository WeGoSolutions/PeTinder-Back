package cruds.Users.init;

import cruds.Users.controller.dto.request.UserRequestCriarDTO;
import cruds.Users.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.time.LocalDate;

@Component
public class UserInitializer {

    private final UserService userService;

    public UserInitializer(UserService userService) {
        this.userService = userService;
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
            userService.createUser(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}