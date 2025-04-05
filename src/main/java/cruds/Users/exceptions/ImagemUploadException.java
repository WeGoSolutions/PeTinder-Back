package cruds.Users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImagemUploadException extends RuntimeException {
    public ImagemUploadException(String message) {
        super(message);
    }
    public ImagemUploadException() {
    }
}