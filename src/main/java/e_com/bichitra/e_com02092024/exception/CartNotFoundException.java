package e_com.bichitra.e_com02092024.exception;

public class CartNotFoundException extends RuntimeException{

    public CartNotFoundException(String msg){
        super(msg);
    }
}
