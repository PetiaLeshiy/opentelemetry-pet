package org.example.config;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanId;
import io.opentelemetry.api.trace.TraceId;
import io.opentelemetry.sdk.internal.RandomSupplier;
import io.opentelemetry.sdk.trace.IdGenerator;
import io.opentelemetry.sdk.trace.ReadableSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomIdGenerator implements IdGenerator {

    private static final long INVALID_ID = 0;
    private static final Supplier<Random> randomSupplier = RandomSupplier.platformDefault();

    @Override
    public String generateSpanId() {
      log.info("|||||||||||__________ START GENERATING SPAN_ID");
        long id;
        Random random = randomSupplier.get();
        do {
            id = random.nextLong();
        } while (id == INVALID_ID);
        return SpanId.fromLong(id);

    }

    @Override
    public String generateTraceId() {

        log.info("|||||||||||__________ START GENERATING TRACE_ID");
        Span current = Span.current();
//        current.getSpanContext()
        String context = Span.current().getSpanContext().toString();
        System.out.println(context);
        Random random = randomSupplier.get();
        long idHi = random.nextLong();
        long idLo;
        do {
            idLo = random.nextLong();
        } while (idLo == INVALID_ID);
        return TraceId.fromLongs(idHi, idLo);
    }
}
