package com.contract.instrument.repository;

import com.contract.instrument.model.Instrument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InstrumentRepository extends MongoRepository<Instrument, String> {
}



