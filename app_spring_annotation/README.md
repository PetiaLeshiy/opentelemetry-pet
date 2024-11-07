VM options:
-javaagent:C:/work/open/demoopentelemetry-pet/app_spring_annotation/build/agent/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=C:/work/open/demoopentelemetry-pet/custom_sampler/build/libs/custom_sampler-0.0.1-SNAPSHOT.jar -Dotel.traces.sampler.arg=0.1 


curl -X GET localhost:8080/ping
