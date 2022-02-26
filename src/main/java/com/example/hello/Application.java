package com.example.hello;

import com.example.common.model.CommonOutput;
import com.example.common.model.ErrorOutput;
import com.example.hello.model.HelloInput;
import com.example.hello.model.HelloOutput;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.MethodHint;
import org.springframework.nativex.hint.SerializationHint;
import org.springframework.nativex.hint.TypeHint;

import java.math.BigDecimal;
import java.math.BigInteger;

//@NativeHint
@SerializationHint(types = {HelloInput.class, HelloOutput.class, CommonOutput.class, ErrorOutput.class,
        // BigDecimalのシリアライズに必要
        BigDecimal.class, BigInteger.class, Number.class, String.class})

@TypeHint(types = SerializationUtils.class, typeNames = "org.apache.commons.lang3.SerializationUtils", methods = @MethodHint(name = "clone", parameterTypes = Object.class))
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
