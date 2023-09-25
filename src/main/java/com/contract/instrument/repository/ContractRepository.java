package com.contract.instrument.repository;

import com.contract.instrument.model.Contract;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface ContractRepository extends MongoRepository<Contract, String> {
}

