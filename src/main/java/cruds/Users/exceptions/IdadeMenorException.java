package cruds.Users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class IdadeMenorException extends RuntimeException {
    public IdadeMenorException(String message) {
        super(message);
    }

    public IdadeMenorException() {
    }
}
