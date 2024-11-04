package org.example;

import com.google.auto.service.AutoService;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.traces.ConfigurableSamplerProvider;
import io.opentelemetry.sdk.trace.samplers.Sampler;

@AutoService(ConfigurableSamplerProvider.class)
public class SpanFilterSamplerProvider implements ConfigurableSamplerProvider {
    @Override
    public Sampler createSampler(ConfigProperties configProperties) {
        double ratio = configProperties.getDouble("otel.traces.sampler.arg", 0.01);
        System.out.println("++++++++++++" + ratio);
        System.out.println("++++++++++++" + configProperties.getString("otel.traces.sampler.attribute.name", "NAME"));


        return CustomTraceIdRatioBasedSampler.create(ratio);
    }

    @Override
    public String getName() {
        return "CustomTraceIdRatioBasedSampler"; // You can replace SpanFilterSampler with a custom name.
    }
}