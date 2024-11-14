package org.example.service;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.*;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.SpringStarter;
import org.example.config.CustomIdGenerator;
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

    private final CustomIdGenerator idGenerator;
    private final OpenTelemetry openTelemetry;
    private Tracer tracer;

    @PostConstruct
    void setup() {
        tracer = openTelemetry.getTracer(SpringStarter.class.getName());
    }

    public void checkMsiSdn(String number) {
        if (storage.contains(number)) {
            Span.current().setAttribute("Did trace finished forcibly", true).end();
            SpanContext remoteContext = SpanContext.create("6f1c2506fd544c506624bbda70acca1d","6f1c2506fd544c50", TraceFlags.getSampled(), TraceState.getDefault());
            Span span = tracer.spanBuilder("Specific_number_trace")
                    .setAttribute("msiSdn", number)
                    .setParent(Context.current().with(Span.wrap(remoteContext)))
                    .startSpan();

            try (Scope scope = span.makeCurrent()) {
                serviceA.serviceADoNothing(number);
            } finally {
                span.end();
            }

        } else {
            //do nothing
        }
    }

}
