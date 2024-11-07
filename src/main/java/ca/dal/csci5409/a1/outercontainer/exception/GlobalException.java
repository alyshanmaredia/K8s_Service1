package ca.dal.csci5409.a1.outercontainer.exception;
import ca.dal.csci5409.a1.outercontainer.constant.Constants;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalException {
    private static final Logger logger = LoggerFactory.getLogger(GlobalException.class);
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String,Object>> serviceException(CustomException exception) {
        Map<String,Object> errorResponse = new LinkedHashMap<>();

        logger.error("CustomException caught - Filename: {}, ErrorMessage: {}, ErrorCode: {}",
                exception.getFilename(), exception.getErrorMsg(), exception.getErrorCode());

        errorResponse.put(Constants.FILE,exception.getFilename());
        errorResponse.put(Constants.ERROR,exception.getErrorMsg());

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(exception.getErrorCode()));
    }
}