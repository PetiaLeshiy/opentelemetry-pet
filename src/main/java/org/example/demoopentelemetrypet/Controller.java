package org.example.demoopentelemetrypet;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class Controller {

    private static final Logger LOGGER = LogManager.getLogger(Controller.class);
    private final AttributeKey<String> ATTR_METHOD = AttributeKey.stringKey("method");

    private final Random random = new Random();
    private final Tracer tracer;
    private final LongHistogram doWorkHistogram;

    @Autowired
    Controller(OpenTelemetry openTelemetry) {
        tracer = openTelemetry.getTracer(Application.class.getName());
        Meter meter = openTelemetry.getMeter(Application.class.getName());
        doWorkHistogram = meter.histogramBuilder("do-work").ofLongs().build();
    }

    @GetMapping("/ping")
    public String ping() throws InterruptedException {
        int sleepTime = random.nextInt(10);
        doWork(sleepTime);
        doWorkHistogram.record(sleepTime, Attributes.of(ATTR_METHOD, "ping"));
        return "pong";
    }

    private void doWork(int sleepTime) throws InterruptedException {
        LOGGER.info("CONTROLLER NUMBER: {}", sleepTime);
        Span span;
        if (sleepTime % 2 == 0) {
            span = tracer.spanBuilder("doWork").setAttribute("number", sleepTime).startSpan();
        } else {
            span = tracer.spanBuilder("doWork").startSpan();
        }
//        int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
//        if (randomNum % 2 != 0) {

//        }
        span.end();
    }
}
