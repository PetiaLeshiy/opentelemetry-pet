package org.example;

import com.google.auto.service.AutoService;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.trace.SdkTracerProviderBuilder;

import java.util.HashMap;
import java.util.Map;

@AutoService(AutoConfigurationCustomizerProvider.class)
public class DemoAutoConfigurationCustomizerProvider implements AutoConfigurationCustomizerProvider {

    @Override
    public void customize(AutoConfigurationCustomizer autoConfiguration) {
        System.out.println("||||||||||||||||||||||||||||||");
        autoConfiguration.addTracerProviderCustomizer(this::configureSdkTracerProvider).addPropertiesSupplier(this::getDefaultProperties);
    }

    private SdkTracerProviderBuilder configureSdkTracerProvider(SdkTracerProviderBuilder tracerProvider, ConfigProperties config) {
        double ratio = config.getDouble("otel.traces.sampler.arg", 0.01);
        System.out.println("+++++++++ RATIO: " + ratio);
        return tracerProvider.setSampler(CustomTraceIdRatioBasedSampler.create(ratio));

//        return tracerProvider.setIdGenerator(new DemoIdGenerator())
//                .setSpanLimits(SpanLimits.builder().setMaxNumberOfAttributes(1024).build())
//                .addSpanProcessor(new DemoSpanProcessor())
//                .addSpanProcessor(SimpleSpanProcessor.create(new DemoSpanExporter()));
    }

    private Map<String, String> getDefaultProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.put("otel.exporter.otlp.traces.endpoint", "http://127.0.0.1:4318/v1/traces");
        properties.put("otel.traces.sampler", "CustomTraceIdRatioBasedSampler"); //todo чекнуть на что влияет


//        properties.put("otel.exporter.otlp.insecure", "true");
//        properties.put("otel.config.max.attrs", "16");
        return properties;
    }
}
//    OTEL_EXPORTER_OTLP_LOGS_ENDPOINT=http://127.0.0.1:4318/v1/logs;
//    OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://127.0.0.1:4318/v1/metrics;
//    OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://127.0.0.1:4318/v1/traces;
//    OTEL_LOGS_EXPORTER=otlp;
//    OTEL_SERVICE_NAME=agent-example-app
