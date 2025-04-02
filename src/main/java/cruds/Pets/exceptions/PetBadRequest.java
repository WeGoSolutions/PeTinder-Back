package cruds.Pets.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class PetBadRequest extends RuntimeException {

    public PetBadRequest(String message, IllegalArgumentException e) {
        super(message);
    }
}

