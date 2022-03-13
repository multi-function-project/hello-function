package com.example.hello;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.example.common.model.CommonOutput;
import com.example.common.model.ErrorOutput;
import com.example.common.util.FileUtil;
import com.example.common.util.StringUtil;
import com.example.hello.model.HelloInput;
import com.example.hello.model.HelloOutput;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.nativex.hint.ResourceHint;
import org.springframework.nativex.hint.SerializationHint;
import org.springframework.nativex.hint.TypeHint;

@SerializationHint(types = { HelloInput.class, HelloOutput.class, CommonOutput.class, ErrorOutput.class,
        // BigDecimalのシリアライズに必要
        BigDecimal.class, BigInteger.class, Number.class, String.class })
@TypeHint(types = { StringUtil.class })
@TypeHint(types = { FileUtil.class })
@ResourceHint(patterns = { "data.csv" })
@ResourceHint(patterns = { "messages.yml" })
@ComponentScan(basePackages = "com.example")
@SpringBootApplication
public class Application {

    /*
     * You need this main method (empty) or explicit
     * <start-class>example.FunctionConfiguration</start-class>
     * in the POM to ensure boot plug-in makes the correct entry
     */
    public static void main(String[] args) {
        // empty unless using Custom runtime at which point it should include
        SpringApplication.run(Application.class, args);
    }
}
