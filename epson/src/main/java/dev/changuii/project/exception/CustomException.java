package dev.changuii.project.exception;

public class CustomException extends RuntimeException{
    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
    }
}
