package cruds.common.event;

import cruds.Users.controller.dto.response.UserResponseCadastroDTO;
import org.springframework.context.ApplicationEvent;

public class UserCreatedEvent extends ApplicationEvent {
    private final UserResponseCadastroDTO user;

    public UserCreatedEvent(Object source, UserResponseCadastroDTO user) {
        super(source);
        this.user = user;
    }

    public UserResponseCadastroDTO getUser() {
        return user;
    }
}