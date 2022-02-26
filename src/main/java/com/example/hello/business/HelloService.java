package com.example.hello.business;

import com.example.hello.model.HelloInput;
import com.example.hello.model.HelloOutput;

public interface HelloService {
    HelloOutput execute(HelloInput input);
}
