VM options:
-javaagent:C:/work/open/demoopentelemetry-pet/app_spring_annotation/build/agent/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=C:/work/open/demoopentelemetry-pet/custom_sampler/build/libs/custom_sampler-0.0.1-SNAPSHOT.jar -Dotel.traces.sampler.arg=0.1 -Dotel.traces.sampler.attribute.name=VASIA

env variables(do not use)
for IDE:
    OTEL_EXPORTER_OTLP_LOGS_ENDPOINT=http://127.0.0.1:4318/v1/logs;OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://127.0.0.1:4318/v1/metrics;OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://127.0.0.1:4318/v1/traces;OTEL_LOGS_EXPORTER=otlp;OTEL_SERVICE_NAME=agent-example-app
as List:
    OTEL_EXPORTER_OTLP_LOGS_ENDPOINT=http://127.0.0.1:4318/v1/logs;
    OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://127.0.0.1:4318/v1/metrics;
    OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://127.0.0.1:4318/v1/traces;
    OTEL_LOGS_EXPORTER=otlp;
    OTEL_SERVICE_NAME=agent-example-app

curl -X GET localhost:8080/ping
