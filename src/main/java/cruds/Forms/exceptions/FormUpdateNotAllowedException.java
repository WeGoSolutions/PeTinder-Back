package cruds.Forms.exceptions;

public class FormUpdateNotAllowedException extends RuntimeException {
    public FormUpdateNotAllowedException(String message) {
        super(message);
    }
}