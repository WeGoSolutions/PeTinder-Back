package cruds.Ong.V2.core.application.exception;

public class OngException extends RuntimeException {
    
    public OngException(String message) {
        super(message);
    }
    
    public OngException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static class OngNaoEncontradaException extends OngException {
        public OngNaoEncontradaException(String message) {
            super(message);
        }
    }
    
    public static class EmailJaExisteException extends OngException {
        public EmailJaExisteException(String message) {
            super(message);
        }
    }
    
    public static class CnpjJaExisteException extends OngException {
        public CnpjJaExisteException(String message) {
            super(message);
        }
    }
    
    public static class CpfJaExisteException extends OngException {
        public CpfJaExisteException(String message) {
            super(message);
        }
    }
    
    public static class SenhaInvalidaException extends OngException {
        public SenhaInvalidaException(String message) {
            super(message);
        }
    }
    
    public static class CredenciaisInvalidasException extends OngException {
        public CredenciaisInvalidasException(String message) {
            super(message);
        }
    }
    
    public static class ImagemNaoEncontradaException extends OngException {
        public ImagemNaoEncontradaException(String message) {
            super(message);
        }
    }
}
