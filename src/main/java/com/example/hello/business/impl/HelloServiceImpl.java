package com.example.hello.business.impl;

import com.example.common.util.StringUtil;
import com.example.hello.business.HelloService;
import com.example.hello.model.HelloInput;
import com.example.hello.model.HelloOutput;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HelloServiceImpl implements HelloService {

    private final StringUtil stringUtil;

    @Override
    public HelloOutput execute(HelloInput input) {
        var output = new HelloOutput();
        output.setMessage(stringUtil.toUpperCase(input.getMessage()) + "ですよ～");
        return output;
    }

}