package com.eggzin.javer_service.exception;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

public class ArgumentosInvalidosException extends BindException {

  public ArgumentosInvalidosException(BindingResult bindingResult) {
        super(bindingResult);
    }

}
