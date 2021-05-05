package com.barlea.hipaa.chaincode;

import com.barlea.common.*;
import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;
import java.time.Period;

@DataType
public final class Patient {

    @Property()
    private final Name name;

    @Property()
    private final String gender;

    @Property
    private final Address address;

    @Property()
    private final Birthdate birthdate;

    public Name getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Address getAddress() {
        return address;
    }

    public Birthdate getBirthdate() {
        return birthdate;
    }

    public int getAge() {
        if (birthdate != null) {
            return Period.between(LocalDate.of(birthdate.getYear(),birthdate.getMonth(),birthdate.getDay()), LocalDate.now()).getYears();
        } else return -1;
    }

    public Patient(@JsonProperty("name") final Name name,
                   @JsonProperty("gender") final String gender,
                   @JsonProperty("address") final Address address,
                   @JsonProperty("birthdate") final Birthdate birthdate) {
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.birthdate = birthdate;
    }


}