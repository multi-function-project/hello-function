package com.example.hello.function;

import com.example.common.exception.BusinessException;
import com.example.common.exception.SystemException;
import com.example.common.model.CommonOutput;
import com.example.common.model.ErrorOutput;
import com.example.hello.business.HelloService;
import com.example.hello.business.HelloValidationService;
import com.example.hello.model.HelloInput;
import com.example.hello.model.HelloOutput;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component("helloFunction")
@Slf4j
@AllArgsConstructor
public class HelloFunction implements Function<Message<HelloInput>, Message<CommonOutput<HelloOutput>>> {

    private final HelloValidationService validationService;
    private final HelloService helloService;

    @Override
    public Message<CommonOutput<HelloOutput>> apply(Message<HelloInput> input) {

        log.info(this.getClass().getName() + " has started!");
        log.info("[input]" + input.toString());

        final CommonOutput<HelloOutput> output = new CommonOutput<>();

        try {

            HelloInput helloInput = input.getPayload();
            // HelloWorldInput cloned = SerializatienableLoggingRequestDetails:
            // trueonUtils.clone(helloWorldInput);
            // SerializationUtils.roundtrip(helloWorldInput);
            // Math.abs(0.2); helloWorldOutput.setMessage(helloWorldInput.getvoidMessage() +
            // "ですよ～");

            // log.info(cloned.toString());

            // 入力検証
            Optional<ErrorOutput> error = validate(helloInput);
            if (error.isPresent()) {
                output.setError(error.get());
                return MessageBuilder.withPayload(output).build();
            }

            HelloOutput helloWorldOutput = helloService.execute(helloInput);
            output.setResult(helloWorldOutput);
            log.info("[Output]" + helloWorldOutput.toString());
            return MessageBuilder.withPayload(output).build();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SystemException("", e.getStackTrace(), e);
        }
    }

    Optional<ErrorOutput> validate(HelloInput input) {
        try {
            validationService.execute(input);
        } catch (BusinessException e) {
            return Optional.of(new ErrorOutput(e.getCode(), e.getMessage()));
        }
        return Optional.empty();
    }
}
