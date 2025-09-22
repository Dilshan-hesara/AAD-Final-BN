package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.exception;

import io.jsonwebtoken.ExpiredJwtException;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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
        @ExceptionHandler(UsernameNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ApiResponseDto handleUserNameNotFoundException(UsernameNotFoundException ex) {
            return new ApiResponseDto(404, "User Not Found", null);
        }

        @ExceptionHandler(BadCredentialsException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ApiResponseDto handleBadCredentials(BadCredentialsException ex) {
            return new ApiResponseDto(400, "Bad Credentials", null);
        }

        @ExceptionHandler(ExpiredJwtException.class)
        @ResponseStatus(HttpStatus.UNAUTHORIZED)
        public ApiResponseDto handleJWTTokenExpiredException(ExpiredJwtException ex) {
            return new ApiResponseDto(401, "JWT Token Expired", null);
        }

        @ExceptionHandler(RuntimeException.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public ApiResponseDto handleAllExceptions(RuntimeException ex) {
            return new ApiResponseDto(500, "Internal Server Error", null);
        }


}