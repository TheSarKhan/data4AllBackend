package org.example.dataprotal.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(ResourceCanNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceCanNotFoundException(ResourceCanNotFoundException resourceCanNotFoundException) {
        ErrorResponse canNotFoundThisResource = new ErrorResponse(resourceCanNotFoundException.getMessage(), "Can not found this resource");
        log.error(resourceCanNotFoundException.getMessage());
        return new ResponseEntity<>(canNotFoundThisResource, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotActiveException.class)
    public ResponseEntity<ErrorResponse> handleNotActiveException(NotActiveException notActiveException) {
        ErrorResponse notOpened = new ErrorResponse(notActiveException.getMessage(), "This data is not opened");
        log.error(notActiveException.getMessage());
        return new ResponseEntity<>(notOpened, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvoiceCanNotBeCreatedException.class)
    public ResponseEntity<ErrorResponse> handleInvoiceCanNotBeCreatedException(InvoiceCanNotBeCreatedException invoiceCanNotBeCreatedException) {
        ErrorResponse canNotFoundThisResource = new ErrorResponse(invoiceCanNotBeCreatedException.getMessage(), "Invoice can not be created");
        log.error(invoiceCanNotBeCreatedException.getMessage());
        return new ResponseEntity<>(canNotFoundThisResource, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception exception) {
        ErrorResponse canNotFoundThisResource = new ErrorResponse(exception.getMessage(), "Internal server error");
        log.error(exception.getMessage());
        return new ResponseEntity<>(canNotFoundThisResource, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
