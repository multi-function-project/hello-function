package com.example.hello.business.impl;

import com.example.hello.business.HelloService;
import com.example.hello.model.HelloInput;
import com.example.hello.model.HelloOutput;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public HelloOutput execute(HelloInput input) {
        var output = new HelloOutput();
        output.setMessage(input.getMessage() + "ですよ～");
        return output;
    }

}