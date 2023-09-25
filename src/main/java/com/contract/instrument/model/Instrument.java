package com.contract.instrument.model;

/*

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Document(collection = "instruments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instrument {

    @Id
    private String tokenId;
    private List<Contract> contracts;
}


 */


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Instrument {
    @Id
    private String id;
    private String name;

    @DBRef
    private List<Contract> contracts;

}


