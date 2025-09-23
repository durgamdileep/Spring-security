package com.dileep.BasicAuthenticationAndAuthorization.Exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public void handlingUserNotFoundException(UsernameNotFoundException ex){
        System.out.println(ex.getMessage());
    }
}
