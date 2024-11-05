package org.example.app_spring_annotation;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class Controller {

    private static final Logger LOGGER = LogManager.getLogger(Controller.class);
    private final Random random = new Random();

    @GetMapping("/ping")
    public String ping() throws InterruptedException {
        int sleepTime = random.nextInt(10);
        doWork(sleepTime);
        return "pong";
    }

    @WithSpan("doWork_with_annotation")
    private void doWork(int sleepTime) throws InterruptedException {
        LOGGER.info("CONTROLLER NUMBER: {}", sleepTime);
        if (sleepTime % 2 == 0) {
            Span.current().setAttribute("number", sleepTime);
        }
    }
}
