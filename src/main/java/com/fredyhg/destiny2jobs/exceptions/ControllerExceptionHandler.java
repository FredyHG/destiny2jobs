package com.fredyhg.destiny2jobs.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.security.auth.login.AccountException;
import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AccountException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage accountAlreadyExists(Exception ex, WebRequest request){
        return createNewErrorMessage(ex , request ,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage emailAlreadyExists(Exception ex, WebRequest request){
        return createNewErrorMessage(ex , request ,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissionException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage missionErro(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.BAD_REQUEST);
    }


    public ErrorMessage createNewErrorMessage(Exception ex, WebRequest request, HttpStatus httpStatus) {
        return ErrorMessage
                .builder()
                .statusCode(httpStatus.value())
                .timestamp(new Date())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();
    }


}
