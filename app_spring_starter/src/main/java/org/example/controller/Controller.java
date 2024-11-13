package org.example.controller;

import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.OpenTelemetryService;
import org.example.service.ServiceA;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequiredArgsConstructor
@Slf4j

public class Controller {
    private final OpenTelemetryService oTService;
    private final ServiceA serviceA;
    private final Random random = new Random();




    @GetMapping("/ping/{id}")
    public String ping(@SpanAttribute @PathVariable("id") String id) throws InterruptedException {
        int conditional = random.nextInt(6);
        Integer msiSdn = null;
//        if (conditional % 2 == 0) {
        msiSdn = 5;
//        }
        doWork(msiSdn);
        return "pong";
    }

    @PostMapping("/add/{msiSdn}") //todo only for testing
    public void addMsiSdn(@PathVariable String msiSdn) throws InterruptedException {
        oTService.getStorage().add(msiSdn);
    }


    @DeleteMapping("/delete/{msiSdn}") //todo only for testing
    public void deleteMsiSdn(@PathVariable String msiSdn) throws InterruptedException {
        oTService.getStorage().remove(msiSdn);
    }

    @GetMapping("/getAll")
    public String getAllMsiSdn() {
        return oTService.getStorage().toString();
    }

    @GetMapping("/pong/{msiSdn}")
    public String pong(@PathVariable String msiSdn) throws InterruptedException {

        oTService.checkMsiSdn(msiSdn);

//        serviceA.serviceADoNothing(msiSdn);
        return "pong";
        }


    private void doWork(Integer msiSdn) {
        log.info("CONTROLLER NUMBER: {}", msiSdn);
        serviceA.serviceADoNothing(String.valueOf(msiSdn));
    }
}