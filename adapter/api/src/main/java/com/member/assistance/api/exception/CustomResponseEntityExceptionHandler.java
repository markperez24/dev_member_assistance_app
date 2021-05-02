package com.member.assistance.api.exception;

import com.member.assistance.core.exception.MemberAssistanceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    public static final Logger LOGGER = LogManager.getLogger(CustomResponseEntityExceptionHandler.class);

    public CustomResponseEntityExceptionHandler() {
        super();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleAllUncaughtException(
            String message,
            RuntimeException exception
    ){
        LOGGER.error("Unknown error occurred", exception);
        return buildErrorResponse(
                exception,
                message,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                httpStatus.value(),
                exception.getMessage()
        );

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * Handle blobs, file, report generations on angular client.
     * @param e
     * @return
     */
    public ResponseEntity<?> handleFileHandlingException(String message, Exception e) {
        if(Objects.isNull(message)) {
            message = e.getMessage();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.setContentDispositionFormData("error", mae.getMessage());
        return new ResponseEntity<>(message, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
