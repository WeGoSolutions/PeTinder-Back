package cruds.common.event;

import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class UserLoggedInEvent extends ApplicationEvent {
    private final UserResponseCadastroDTO user;
    private final LocalDateTime loginTime;

    public UserLoggedInEvent(Object source, UserResponseCadastroDTO user, LocalDateTime loginTime) {
        super(source);
        this.user = user;
        this.loginTime = loginTime;
    }

    public UserResponseCadastroDTO getUser() {
        return user;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }
}