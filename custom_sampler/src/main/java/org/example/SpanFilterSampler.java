package org.example;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingDecision;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpanFilterSampler implements Sampler {

    /*
     * Filter out spans whose name is in EXCLUDED_SPAN_NAMES.
     */
    private static List<String> EXCLUDED_SPAN_NAMES = Collections.unmodifiableList(
            Arrays.asList("spanName1", "spanName2")
    );

    /*
     * Filter out spans whose attributes.http.target is in EXCLUDED_HTTP_REQUEST_TARGETS.
     */
    private static List<String> EXCLUDED_HTTP_REQUEST_TARGETS = Collections.unmodifiableList(
            Arrays.asList("/api/checkHealth", "/health/checks")
    );

    @Override
    public SamplingResult shouldSample(Context parentContext, String traceId, String name, SpanKind spanKind, Attributes attributes, List<LinkData> parentLinks) {
//        String httpTarget = attributes.get(SemanticAttributes.HTTP_TARGET) != null ? attributes.get(SemanticAttributes.HTTP_TARGET) : "";



        System.out.println("++++++++++++++++++++++ DEFAULT SAMPLER +++++++++++++++++++++");
//        if (EXCLUDED_SPAN_NAMES.contains(name) || EXCLUDED_HTTP_REQUEST_TARGETS.contains(httpTarget)) { // Filter out spans based on the rules.
//            return SamplingResult.create(SamplingDecision.DROP);
//        } else {
            return SamplingResult.create(SamplingDecision.RECORD_AND_SAMPLE);
//        }
    }

    @Override
    public String getDescription() {
        return "SpanFilterSampler"; // You can replace SpanFilterSampler with a custom name.
    }
}