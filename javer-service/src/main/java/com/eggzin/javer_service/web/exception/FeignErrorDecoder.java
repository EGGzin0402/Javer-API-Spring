package com.eggzin.javer_service.web.exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import com.eggzin.javer_service.exception.*;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
    public Exception decode(String methodKey, Response response) {
		
		try {
			Reader reader = new InputStreamReader(response.body().asInputStream());
			
			String responseBody = readReaderToString(reader);
			
			JsonNode jsonNode = objectMapper.readTree(responseBody);
			
			String message = jsonNode.has("message") ? jsonNode.get("message").asText() : "Unknown error";

            switch (response.status()) {
                case 400 -> {
                    return new PasswordInvalidException(message);
                }
                case 401 -> {
                    return new UnauthorizedException(message);
                }
                case 403 -> {
                    return new ForbiddenException(message);
                }
                case 404 -> {
                    return new EntityNotFoundException(message);
                }
                case 409 -> {
                    return new UsernameUniqueViolationException(message);
                }
                case 422 -> {

                    List<FieldError> fieldErrors = new ArrayList<>();
                    if (jsonNode.has("errors")) {
                        JsonNode errorsNode = jsonNode.get("errors");

                        for (JsonNode errorNode : errorsNode) {
                            String field = errorNode.has("field") ? errorNode.get("field").asText() : "Unknown field";
                            String errorMessage = errorNode.has("message") ? errorNode.get("message").asText() : "Unknown error";
                            fieldErrors.add(new FieldError("object", field, errorMessage));
                        }
                    }

                    BindingResult bindingResult = new BindException(new Object(), "object");
                    for (FieldError fieldError : fieldErrors) {
                        bindingResult.addError(fieldError);
                    }

                    return new ArgumentosInvalidosException(bindingResult);
                }
            }
            return new RuntimeException(response.reason());
		} catch (IOException e) {
			 return new Exception("Erro ao ler o corpo da resposta: " + e.getMessage());
		}
    }
	
    private String readReaderToString(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }

}
