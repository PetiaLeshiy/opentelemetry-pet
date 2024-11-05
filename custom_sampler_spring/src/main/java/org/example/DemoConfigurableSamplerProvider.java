/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.example;

import com.google.auto.service.AutoService;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.traces.ConfigurableSamplerProvider;
import io.opentelemetry.sdk.trace.samplers.Sampler;

@AutoService(ConfigurableSamplerProvider.class)
public class DemoConfigurableSamplerProvider implements ConfigurableSamplerProvider {

  @Override
  public Sampler createSampler(ConfigProperties config) {
    double ratio = config.getDouble("otel.traces.sampler.arg", 0.01);
    System.out.println("+++++++++ RATIO: " + ratio);
    return CustomTraceIdRatioBasedSampler.create(ratio);
  }

  @Override
  public String getName() {
    return "CustomTraceIdRatioBasedSampler";
  }
}
