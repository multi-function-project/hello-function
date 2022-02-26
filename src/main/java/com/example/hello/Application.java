package com.example.hello;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.example.common.model.CommonOutput;
import com.example.common.model.ErrorOutput;
import com.example.common.util.StringUtil;
import com.example.hello.model.HelloInput;
import com.example.hello.model.HelloOutput;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.nativex.hint.SerializationHint;
import org.springframework.nativex.hint.TypeHint;

@SerializationHint(types = {HelloInput.class, HelloOutput.class, CommonOutput.class, ErrorOutput.class,
        // BigDecimalのシリアライズに必要
        BigDecimal.class, BigInteger.class, Number.class, String.class})
@TypeHint(types = {StringUtil.class})
@ComponentScan(basePackages = "com.example")
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
