package jm.pp.rescuer313.controller;

import jm.pp.rescuer313.ExeptionHandler.DataInfoHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<DataInfoHandler> handleClientException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body(new DataInfoHandler(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DataInfoHandler> handleServerException(Exception e) {
        return ResponseEntity.status(500).body(new DataInfoHandler(e.getMessage()));
    }
}