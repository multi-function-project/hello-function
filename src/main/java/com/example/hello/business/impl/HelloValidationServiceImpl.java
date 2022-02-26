package com.example.hello.business.impl;

import com.example.common.exception.BusinessException;
import com.example.hello.business.HelloValidationService;
import com.example.hello.model.HelloInput;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.Objects;

@Service
@AllArgsConstructor
public class HelloValidationServiceImpl implements HelloValidationService {

    private final MessageSource messageSource;

    @Override
    public void execute(HelloInput input) throws BusinessException {
        if (Objects.isNull(input) || !StringUtils.hasText(input.getMessage())) {
            throw new BusinessException("BE00001", messageSource.getMessage("BE0001", new Object[]{"メッセージ"}, Locale.getDefault()));
        }
    }
}
