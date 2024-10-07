package e_com.bichitra.e_com02092024.exception;

public class APIException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public APIException() {
        super();
    }
    public APIException(String id,String message) {
        super(id);
    }

    public APIException(String message) {
        super(message);
    }
}
