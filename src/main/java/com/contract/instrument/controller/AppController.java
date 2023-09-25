package com.contract.instrument.controller;

import com.contract.instrument.model.Contract;
import com.contract.instrument.model.Instrument;
import com.contract.instrument.service.AppService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private AppService appService;

    @PostMapping("/instrument")
    public Instrument addInstrument(@RequestParam String instrumentName) {
        return appService.addInstrument(instrumentName);
    }

    @GetMapping("/instruments")
    public List<Instrument> getAllInstruments() {
        return appService.getAllInstruments();
    }


    @PostMapping("/contract")
    public Contract createContract(@RequestParam String contractName) {
        return appService.createContract(contractName);
    }

    @GetMapping("/contracts")
    public List<Contract> getAllContracts() {
        return appService.getAllContracts();
    }

    @GetMapping("/{contractId}")
    public Contract getContractById(@PathVariable String contractId) {
        return appService.getContractById(contractId);
    }


    @PostMapping("/instrument/{instrumentId}/contract/{contractId}")
    public Instrument addContractToInstrument(@PathVariable String instrumentId, @PathVariable String contractId) {
        return appService.addContractToInstrument(instrumentId, contractId);
    }

    @GetMapping("/instrument/{instrumentId}/contracts")
    public List<Contract> getContractsByInstrument(@PathVariable String instrumentId) {
        return appService.getContractsByInstrument(instrumentId);
    }
}

