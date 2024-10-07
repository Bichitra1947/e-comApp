package e_com.bichitra.e_com02092024.exception;

import e_com.bichitra.e_com02092024.apiResponse.APICategoryResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> response = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            response.put(fieldName,errorMessage);
        });
        return new ResponseEntity<Map<String, String>>(response,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> myResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("errors", ex.getValidationErrors());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APICategoryResponse> myAPIException(APIException e){
        final String message = e.getMessage();
        APICategoryResponse apiCategoryResponse=new APICategoryResponse(message,"Wrong request please try to correct it");
        return new ResponseEntity<>(apiCategoryResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<APICategoryResponse> myCategoryNotFoundException(CategoryNotFoundException ex){
        final String message = ex.getMessage();
        APICategoryResponse apiCategoryResponse=new APICategoryResponse(message,"Wrong request please try to correct it");
        return new ResponseEntity<>(apiCategoryResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestFoundException.class)
   public ResponseEntity<String> myResponseStatusException(BadRequestFoundException e){
        final String message = e.getMessage();
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> allException(Exception ex) {
//        final String message = ex.getMessage();
//        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
//    }

}
