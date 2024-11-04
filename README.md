VM options:
-javaagent:C:/work/open/opentelemetry-java-examples/javaagent/build/agent/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=C:/work/open/opentelemetry-java-examples/javaagent/build/agent/custom_sampler-1.0-SNAPSHOT.jar -Dotel.traces.sampler=CustomTraceIdRatioBasedSampler -Dotel.traces.sampler.arg=0.1 -Dotel.traces.sampler.attribute.name=VASIA

env variables:
OTEL_EXPORTER_OTLP_LOGS_ENDPOINT=http://127.0.0.1:4318/v1/logs;OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://127.0.0.1:4318/v1/metrics;OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://127.0.0.1:4318/v1/traces;OTEL_LOGS_EXPORTER=otlp;OTEL_SERVICE_NAME=agent-example-app