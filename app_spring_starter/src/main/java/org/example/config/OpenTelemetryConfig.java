package org.example.config;


import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.trace.IdGenerator;
import io.opentelemetry.sdk.trace.SdkTracerProviderBuilder;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.sdk.trace.samplers.Sampler;

import java.util.Collections;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OpenTelemetryConfig {

//    @Bean("customSampler")
//    public Sampler customSampler() {
//        double ratio = 0.01;
//        log.info("+++++++ RATIO: {}", ratio);
//        return CustomSampler.create(ratio);
//    }

    @Bean
    public AutoConfigurationCustomizerProvider otelCustomizer(@Qualifier("customSampler") Sampler customSampler, @Qualifier("customIdGenerator") IdGenerator customIdGenerator) { //todo
        return p ->
//                p.addSamplerCustomizer((a, b) -> create(customSampler, b))
                        p.addTracerProviderCustomizer((a, b) -> traceProvider(a, b, customSampler, customIdGenerator));
//                        .addSpanExporterCustomizer(this::configureSpanExporter);
    }

    private SdkTracerProviderBuilder traceProvider(SdkTracerProviderBuilder sdkTracerProviderBuilder, ConfigProperties configProperties,
                                                   Sampler customSampler, IdGenerator customIdGenerator) {
        log.info("__________________________");
        return sdkTracerProviderBuilder.setIdGenerator(customIdGenerator).setSampler(customSampler);
    }


    private Sampler create(Sampler sampler, ConfigProperties configProperties) {
        return sampler;
    }


    /** suppress spans for actuator endpoints */
//  private RuleBasedRoutingSampler configureSampler(Sampler fallback, ConfigProperties config) {
//    return RuleBasedRoutingSampler.builder(SpanKind.SERVER, fallback)
//        .drop(UrlAttributes.URL_PATH, "^/actuator")
//        .build();
//  }

    /**
     * Configuration for the OTLP exporter. This configuration will replace the default OTLP exporter,
     * and will add a custom header to the requests.
     */
    private SpanExporter configureSpanExporter(SpanExporter exporter, ConfigProperties config) {
        if (exporter instanceof OtlpHttpSpanExporter) {
            return ((OtlpHttpSpanExporter) exporter).toBuilder().setHeaders(this::headers).build();
        }
        return exporter;
    }

    private Map<String, String> headers() {
        return Collections.singletonMap("Authorization", "Bearer " + refreshToken());
    }

    private String refreshToken() {
        // e.g. read the token from a kubernetes secret
        return "token";
    }
}
