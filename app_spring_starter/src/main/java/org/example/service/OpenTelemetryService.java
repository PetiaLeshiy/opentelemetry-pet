package org.example.service;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.SpringStarter;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
@Slf4j
public class OpenTelemetryService {


    @Getter
    private final ConcurrentHashMap.KeySetView<String, Boolean> storage = ConcurrentHashMap.newKeySet();

    @PostConstruct
    private void seetup() {
        storage.add("9215659484");
    }

    private final ServiceA serviceA;

    private final OpenTelemetry openTelemetry;
    private Tracer tracer;

    @PostConstruct
    void setup() {
        tracer = openTelemetry.getTracer(SpringStarter.class.getName());
    }

    public void checkMsiSdn(String number) {
        if (storage.contains(number)) {
            Span.current().setAttribute("Did trace finished forcibly", true).end();



            try (Scope scope = tracer.spanBuilder("Specific_number_trace")
                    .setAttribute("msiSdn", number)
                    .setNoParent()
                    .startSpan()
                    .makeCurrent()) {
                serviceA.serviceADoNothing(number);
            }
        } else {
            //do nothing
        }
    }

}
