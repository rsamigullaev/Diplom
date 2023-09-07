package ru.rus.cs.web.handler;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rus.cs.exception.BadCredentialsException;
import ru.rus.cs.exception.FileCloudException;
import ru.rus.cs.exception.InputDataException;
import ru.rus.cs.exception.UnauthorizedException;
import ru.rus.cs.web.model.ExceptionWebResponse;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionWebResponse> handleBadCredentialsException(@NonNull final BadCredentialsException exc) {
        log.error(exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionWebResponse(exc.getMessage(), 400));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionWebResponse> handleUnauthorizedException(@NonNull final UnauthorizedException exc) {
        log.error(exc.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionWebResponse(exc.getMessage(), 401));
    }

    @ExceptionHandler(InputDataException.class)
    public ResponseEntity<ExceptionWebResponse> handleInputDataException(@NonNull final InputDataException exc) {
        log.error(exc.getMessage());
        return new ResponseEntity<>(new ExceptionWebResponse(exc.getMessage(),400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileCloudException.class)
    public ResponseEntity<ExceptionWebResponse> handleFileCloudException(@NonNull final FileCloudException exc) {
        log.error(exc.getMessage());
        return new ResponseEntity<>(new ExceptionWebResponse(exc.getMessage(),500), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
