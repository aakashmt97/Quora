package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    // This Handler is handling the InvalidQuestionException if it occurs anywhere throughout the program.
    @ExceptionHandler(InvalidQuestionException.class)
    public ResponseEntity<ErrorResponse> invalidQuestionException(InvalidQuestionException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    // This Handler is handling the AuthorizationFailedException if it occurs anywhere throughout the program.
    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException afe, WebRequest webRequest){
        return new  ResponseEntity<ErrorResponse>(new ErrorResponse().code(afe.getCode()).message(afe.getErrorMessage()), HttpStatus.FORBIDDEN);
    }

    // This Handler is handling the SignUpRestrictedException if it occurs anywhere throughout the program.
    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestrictedException(SignUpRestrictedException signUpE, WebRequest webRequest){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(signUpE.getCode()).message(signUpE.getErrorMessage()), HttpStatus.CONFLICT);
    }

    // This Handler is handling the AuthenticationFailedException if it occurs anywhere throughout the program.
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException afe, WebRequest webRequest){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(afe.getCode()).message(afe.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }

    // This Handler is handling the SignOutRestrictedException if it occurs anywhere throughout the program.
    @ExceptionHandler(SignOutRestrictedException.class)
    public ResponseEntity<ErrorResponse> signOutRestrictedException(SignOutRestrictedException signOut, WebRequest webRequest){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(signOut.getCode()).message(signOut.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }

    // This Handler is handling the AnswerNotFoundException if it occurs anywhere throughout the program.
    @ExceptionHandler(AnswerNotFoundException.class)
    public ResponseEntity<ErrorResponse> answerNotFoundException(AnswerNotFoundException anfe, WebRequest webRequest){
        return new  ResponseEntity<ErrorResponse>(new ErrorResponse().code(anfe.getCode()).message(anfe.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    // This Handler is handling the UserNotFoundException if it occurs anywhere throughout the program.
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException ufe, WebRequest webRequest){
        return new  ResponseEntity<ErrorResponse>(new ErrorResponse().code(ufe.getCode()).message(ufe.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

}
