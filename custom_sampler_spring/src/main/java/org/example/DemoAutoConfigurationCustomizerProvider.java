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
        autoConfiguration.addTracerProviderCustomizer(this::configureSdkTracerProvider).addPropertiesSupplier(this::getDefaultProperties);
    }

    private SdkTracerProviderBuilder configureSdkTracerProvider(SdkTracerProviderBuilder tracerProvider, ConfigProperties config) {
        double ratio = config.getDouble("otel.traces.sampler.arg", 0.1);
        System.out.println("|||||||||||||||||||| RATIO: " + ratio);
        return tracerProvider.setSampler(CustomTraceIdRatioBasedSampler.create(ratio));
    }

    private Map<String, String> getDefaultProperties() {
        Map<String, String> properties = new HashMap<>();


        properties.put("otel.exporter.otlp.endpoint", "http://127.0.0.1:4318");
        properties.put("otel.exporter.otlp.insecure", "true");
        properties.put("otel.config.max.attrs", "16");
        properties.put("otel.service.name", "my-app");
        properties.put("otel.metrics.exporter", "none"); //todo не работает
        properties.put("otel.logs.exporter", "none");    //todo не работает

        return properties;
    }
}
