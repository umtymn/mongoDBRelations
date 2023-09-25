package com.contract.instrument.service;


import com.contract.instrument.model.Contract;
import com.contract.instrument.model.Instrument;

import java.util.List;
public interface AppService {
    Instrument addInstrument(String instrumentName);
    List<Instrument> getAllInstruments();

    // Yeni metotlar ekleyin:
    Instrument addContractToInstrument(String instrumentId, String contractId);

    List<Contract> getContractsByInstrument(String instrumentId);

    Contract createContract(String contractName);

    List<Contract> getAllContracts();

    Contract getContractById(String contractId);

}
