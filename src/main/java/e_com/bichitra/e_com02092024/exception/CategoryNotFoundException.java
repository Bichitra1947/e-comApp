package e_com.bichitra.e_com02092024.exception;

public class CategoryNotFoundException extends RuntimeException{

    public CategoryNotFoundException(){
        super();
    }
    public CategoryNotFoundException(String message){
        super(message);
    }
}
