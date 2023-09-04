package guru.springframework.msscbeerservice.web.controller;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
