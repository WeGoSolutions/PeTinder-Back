package cruds.Pets.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PetBadRequest extends RuntimeException {

    public PetBadRequest(String message, IllegalArgumentException e) {
        super(message);
    }
}

