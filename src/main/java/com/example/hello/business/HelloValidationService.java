package com.example.hello.business;

import com.example.common.exception.BusinessException;
import com.example.hello.model.HelloInput;

public interface HelloValidationService {
    public void execute(HelloInput input) throws BusinessException;
}

