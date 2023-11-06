package com.fredyhg.destiny2jobs.exceptions;

import com.fredyhg.destiny2jobs.exceptions.customPackage.CustomPackageAlreadyExistsException;
import com.fredyhg.destiny2jobs.exceptions.customPackage.CustomPackageException;
import com.fredyhg.destiny2jobs.exceptions.customPackage.CustomPackageNotFoundException;
import com.fredyhg.destiny2jobs.exceptions.mission.CustomPackageAlreadyAccepted;
import com.fredyhg.destiny2jobs.exceptions.mission.MissionAlreadyException;
import com.fredyhg.destiny2jobs.exceptions.mission.MissionException;
import com.fredyhg.destiny2jobs.exceptions.mission.MissionNotFoundException;
import com.fredyhg.destiny2jobs.exceptions.user.UserAlreadyExistsException;
import com.fredyhg.destiny2jobs.exceptions.user.UserException;
import com.fredyhg.destiny2jobs.exceptions.user.UserNotAllowedException;
import com.fredyhg.destiny2jobs.exceptions.user.UserNotFoundException;
import com.fredyhg.destiny2jobs.utils.models.ErrorMessage;
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
    public ErrorMessage genericAccountException(Exception ex, WebRequest request){
        return createNewErrorMessage(ex , request ,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage genericUserException(Exception ex, WebRequest request){
        return createNewErrorMessage(ex , request ,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissionException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage genericMissionException(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomPackageException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage genericCustomPackageException(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomPackageNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage customPackageNotFound(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomPackageAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage customPackageAlreadyExists(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage userNotFound(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage userAlreadyExists(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MissionNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage missionNotFound(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissionAlreadyException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage missionAlreadyExists(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotAllowedException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage userNotAllowedException(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomPackageAlreadyAccepted.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage customPackageAlreadyAccepted(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
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
