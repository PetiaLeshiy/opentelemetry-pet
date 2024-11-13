package org.example.service;

import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceA {
    private final ServiceB serviceB;

    @WithSpan("ServiceA_DoNothing")
    public void serviceADoNothing(@SpanAttribute("msi_sdn")String msiSdn) {
        log.info("serviceA");
        serviceB.serviceBDoNothing();
    }
}
