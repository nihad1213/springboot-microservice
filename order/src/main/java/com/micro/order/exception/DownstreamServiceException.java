package com.micro.order.exception;

/** Thrown when another microservice cannot be reached (down, timeout, ...). */
public class DownstreamServiceException extends RuntimeException {
    public DownstreamServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
