package com.example.hello.business.impl;

import java.io.IOException;

import com.example.common.exception.SystemException;
import com.example.common.util.FileUtil;
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
    private final FileUtil fileUtil;

    @Override
    public HelloOutput execute(HelloInput input) {

        try {
            fileUtil.readCsv();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SystemException();
        }

        var output = new HelloOutput();
        output.setMessage(stringUtil.toUpperCase(input.getMessage()) + "ですよ～");
        return output;
    }

}