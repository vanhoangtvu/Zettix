package net.zettix.accountcenter.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionPublic {
    @ExceptionHandler(value = RuntimeException.class)//bat loi runtime
    ResponseEntity<String> handlingRuntimeException(RuntimeException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());

    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)//bat loi validate
    ResponseEntity<String> handlingValidException(MethodArgumentNotValidException exception){
        return ResponseEntity.badRequest().body(exception.getFieldError().getDefaultMessage());
    }

}
