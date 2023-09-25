package com.contract.instrument.service;
import com.contract.instrument.model.Contract;
import com.contract.instrument.model.Instrument;
import com.contract.instrument.repository.ContractRepository;
import com.contract.instrument.repository.InstrumentRepository;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Override
    public Instrument addInstrument(String instrumentName) {
        Instrument instrument = new Instrument();

        if (instrument.getContracts() != null){
            instrument.setName(instrumentName);
            return instrumentRepository.save(instrument);
        }else {
            throw new EntityNotFoundException("You can not create a instrument without contract");
        }

    }

    @Override
    public List<Instrument> getAllInstruments() {
        return instrumentRepository.findAll();
    }

    @Override
    public Instrument addContractToInstrument(String instrumentId, String contractId) {
        Instrument instrument = instrumentRepository.findById(instrumentId)
                .orElseThrow(() -> new EntityNotFoundException("Instrument not found"));
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found"));

        instrument.getContracts().add(contract);
        return instrumentRepository.save(instrument);
    }

    @Override
    public List<Contract> getContractsByInstrument(String instrumentId) {
        Instrument instrument = instrumentRepository.findById(instrumentId)
                .orElseThrow(() -> new EntityNotFoundException("Instrument not found"));
        return instrument.getContracts();
    }

    @Override
    public Contract createContract(String contractName) {
        Contract contract = new Contract();
        contract.setContractName(contractName);
        return contractRepository.save(contract);
    }

    @Override
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    @Override
    public Contract getContractById(String contractId) {
        return contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found"));
    }

}

