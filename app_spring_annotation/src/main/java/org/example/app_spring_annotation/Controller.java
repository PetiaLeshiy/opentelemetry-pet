package org.example.app_spring_annotation;

import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {

    private final ServiceA serviceA;
    private final Random random = new Random();

    @GetMapping("/ping")
    public String ping() throws InterruptedException {
        int conditional = random.nextInt(6);
        Integer msiSdn = null;
        if (conditional % 2 == 0) {
            msiSdn = 5;
        }
        doWork(msiSdn);
        return "pong";
    }

    @WithSpan("doWork_with_annotation")
    private void doWork(@SpanAttribute("msi_sdn") Integer msiSdn) {
        log.info("CONTROLLER NUMBER: {}", msiSdn);
        serviceA.serviceADoNothing();
    }

}
