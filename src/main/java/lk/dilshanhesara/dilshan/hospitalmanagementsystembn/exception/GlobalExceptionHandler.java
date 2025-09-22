package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.exception;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handelGenericException(Exception e) {
        return new ResponseEntity(new ApiResponseDto(500, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto> handelResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity(new ApiResponseDto(404, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}