package cruds.Users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserBadRequest extends RuntimeException {

    public UserBadRequest(String message, IllegalArgumentException e) {
        super(message);
    }

    public UserBadRequest(String message) {
        super(message);
    }
}