package com.outreach.airflowpoc.controller;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.outreach.airflowpoc.dto.GenericResponse;
import com.outreach.airflowpoc.exception.AirflowException;


@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler  {

	 protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	                                                                  HttpHeaders headers,
	                                                                  HttpStatus status,
	                                                                  WebRequest request) {
	        HashMap<String, String> mapOfErrors = new HashMap<String, String>();
	        
	        
	        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
	        	String rejectedValue = fieldError.getRejectedValue().toString();
	        	String defaultMsg = fieldError.getDefaultMessage();
	            
	        	String key = fieldError.getField();
	            if(key.contains(".")){
	                StringTokenizer st = new StringTokenizer(key, ".");
	                st.nextElement();
	                key = st.nextElement().toString();
	            }
	            ((HashMap) mapOfErrors).put(key, rejectedValue);
	        });
	        GenericResponse genericResponse = new GenericResponse("Validation Failed", mapOfErrors);
	        return new ResponseEntity(genericResponse, HttpStatus.BAD_REQUEST);
	    }

	    @ExceptionHandler(AirflowException.class)
	    public final ResponseEntity<Object> handleLoanBoardingException(AirflowException ex, WebRequest request) {
	    	System.out.println("Exception: "+ ex.getMessage());
	        GenericResponse genericResponse = new GenericResponse(ex.getMessage(), new HashMap<>());
	        return new ResponseEntity<>(genericResponse, HttpStatus.BAD_REQUEST);
	    }
	    
}
