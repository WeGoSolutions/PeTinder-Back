package cruds.Users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class UserVazioException extends RuntimeException {

    public UserVazioException(String message) {
        super(message);
    }
}