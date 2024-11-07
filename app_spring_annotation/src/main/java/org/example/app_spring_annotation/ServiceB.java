package org.example.app_spring_annotation;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ServiceB {

    @WithSpan("ServiceA_DoNothing")
    public void serviceBDoNothing() {
        log.info("serviceB");
    }
}
