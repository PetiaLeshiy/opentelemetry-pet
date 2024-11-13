package org.example.config;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.internal.OtelEncodingUtils;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingDecision;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class CustomSampler implements Sampler {

    private static final SamplingResult POSITIVE_SAMPLING_RESULT = SamplingResult.recordAndSample();
    private static final SamplingResult NEGATIVE_SAMPLING_RESULT = SamplingResult.drop();
    private static final String MSI_SDN = "msi_sdn";

    private long idUpperBound;

    @PostConstruct
    private void setup() {
        double ratio = 0.1;
        log.info("+++++++ RATIO: {}", ratio);
        if (ratio < 0.0 || ratio > 1.0) {
            throw new IllegalArgumentException("ratio must be in range [0.0, 1.0]");
        }
        long idUpperBound;

        if (ratio == 0.0) {
            idUpperBound = Long.MIN_VALUE;
        } else if (ratio == 1.0) {
            idUpperBound = Long.MAX_VALUE;
        } else {
            idUpperBound = (long) (ratio * Long.MAX_VALUE);
        }
        this.idUpperBound = idUpperBound;
    }

    @Override
    public SamplingResult shouldSample(
            Context parentContext,
            String traceId,
            String name,
            SpanKind spanKind,
            Attributes attributes,
            List<LinkData> parentLinks) {
        //todo remove unuseful metrics and logs (should find the way to turn off it using properties
        Map<AttributeKey<?>, Object> attr = attributes.asMap();
        Optional<Map.Entry<AttributeKey<?>, Object>> logsMetrics =
                attr.entrySet().stream()
                        .filter(entry -> entry.getKey().getKey().equals("url.full"))
                        .filter(entry -> entry.getValue().toString().endsWith("logs") || entry.getValue().toString().endsWith("metrics"))
                        .findFirst();

        if (logsMetrics.isPresent()) {
            return NEGATIVE_SAMPLING_RESULT;
        }

        if (parentContext != null) {
            String contextAsString = parentContext.toString();
            if (contextAsString.contains(MSI_SDN)) {
                String substring = contextAsString.substring(contextAsString.indexOf(MSI_SDN) + 8);
                String value = substring.substring(0, substring.indexOf(", "));
                return createSamplingResult(value);
            }

        }
        Optional<Map.Entry<AttributeKey<?>, Object>> specificKey =
                attr.entrySet().stream()
                        .filter(entry -> entry.getKey().getKey().equals(MSI_SDN))
                        .filter(entry -> Objects.nonNull(entry.getValue()))
                        .findFirst();
        if (specificKey.isPresent()) {
            return POSITIVE_SAMPLING_RESULT;
        }
            return POSITIVE_SAMPLING_RESULT;


//        return Math.abs(getTraceIdRandomPart(traceId)) < idUpperBound
//                ? POSITIVE_SAMPLING_RESULT
//                : NEGATIVE_SAMPLING_RESULT;
    }

    private static SamplingResult createSamplingResult(String value) {
        return SamplingResult.create(SamplingDecision.RECORD_AND_SAMPLE,
                Attributes.of(AttributeKey.stringKey(MSI_SDN), value));
    }


    @Override
    public String getDescription() {
        return "CustomSampler";
    }


    private static long getTraceIdRandomPart(String traceId) {
        return OtelEncodingUtils.longFromBase16String(traceId, 16);
    }

}
