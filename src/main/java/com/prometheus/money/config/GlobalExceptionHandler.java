package com.prometheus.money.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import io.jsonwebtoken.security.SignatureException;

@ControllerAdvice
public class GlobalExceptionHandler {
	// 处理自定义异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex, WebRequest request) {
    	ex.printStackTrace();
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    // 处理自定义异常
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> handleCustomException(SignatureException ex, WebRequest request) {

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
