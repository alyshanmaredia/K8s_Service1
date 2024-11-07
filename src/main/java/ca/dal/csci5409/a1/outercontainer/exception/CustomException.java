package ca.dal.csci5409.a1.outercontainer.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomException extends Exception {
    private final int errorCode;
    private final String errorMsg;
    private final String filename;

    public CustomException(HttpStatus statusCode,String message, String filename){
        this.errorCode = statusCode.value();
        this.errorMsg = message;
        this.filename = filename;
    }
}


