package com.aanshik.AccountService.ExceptionHandling;

import com.aanshik.AccountService.Utils.Constants;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleExceptions(ResourceNotFoundException ex) {
        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Object>(
                apiError, apiError.getStatus());
    }

    @ExceptionHandler(LowBalanceException.class)
    public ResponseEntity<Object> lowBalanceHandler(LowBalanceException ex) {
        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Object>(
                apiError, apiError.getStatus());
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode httpStatus, WebRequest request) {
        Map<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("Current Timestamp", new Date());
        errorMap.put("Status", httpStatus.value());

        // Get all errors
        List<String> exceptionalErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        errorMap.put("Errors", exceptionalErrors);

        return new ResponseEntity<>(errorMap, httpStatus);
    }


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> sqlExceptionHandler(SQLIntegrityConstraintViolationException ex) {
        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<Object>(
                apiError, apiError.getStatus());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex) throws JSONException {

        if (ex.getStatusCode().is4xxClientError()) {

            //Logic to filter message
            String error=ex.getMessage();
            String exMessage = error.substring(ex.getMessage().indexOf(':') + 1).trim();
            exMessage = exMessage.substring(1, exMessage.length() - 1);


            ApiError apiError =
                    new ApiError(HttpStatus.BAD_REQUEST, exMessage, exMessage);

            return new ResponseEntity<Object>(
                    apiError, apiError.getStatus());


        } else if (ex.getStatusCode().is5xxServerError()) {

            String message = Constants.SERVER_DOWN_MESSAGE;

            ApiError apiError =
                    new ApiError(HttpStatus.BAD_REQUEST, message, message);

            return new ResponseEntity<Object>(
                    apiError, apiError.getStatus());

        }


        return null;

    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex) {

        String s = ex.getMessage()+Constants.SERVER_DOWN_MESSAGE;

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, s, s);
        return new ResponseEntity<Object>(
                apiError, apiError.getStatus());

    }


}