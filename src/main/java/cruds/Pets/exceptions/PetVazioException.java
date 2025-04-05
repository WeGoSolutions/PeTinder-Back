package cruds.Pets.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class PetVazioException extends RuntimeException {

        public PetVazioException(String message) {
            super(message);
        }
}
