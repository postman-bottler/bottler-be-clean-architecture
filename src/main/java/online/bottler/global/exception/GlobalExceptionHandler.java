package online.bottler.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import online.bottler.global.response.ApiResponse;
import online.bottler.global.response.code.ErrorStatus;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonForbiddenException.class)
    public ApiResponse<?> handleCommonForbiddenException(CommonForbiddenException e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        return ApiResponse.onFailure(ErrorStatus.FORBIDDEN.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ApiResponse<?> handleInvalidLoginException(InvalidLoginException e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        return ApiResponse.onFailure(ErrorStatus.INVALID_LOGIN.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(DomainException.class)
    public ApiResponse<?> handleDomainException(DomainException e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        return ApiResponse.onFailure(ErrorStatus.COMMON_BAD_REQUEST.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(ApplicationException.class)
    public ApiResponse<?> handleApplicationException(ApplicationException e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        return ApiResponse.onFailure(ErrorStatus.COMMON_BAD_REQUEST.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(AdaptorException.class)
    public ApiResponse<?> handleAdaptorException(AdaptorException e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        return ApiResponse.onFailure(ErrorStatus.COMMON_BAD_REQUEST.getCode(), e.getMessage(), null);
    }

}
