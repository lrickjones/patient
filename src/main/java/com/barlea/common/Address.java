package com.barlea.common;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType
public class Address {

    @Property()
    private final String address1;

    @Property()
    private final String address2;

    @Property()
    private final String city;

    @Property()
    private final String state;

    @Property()
    private final String zip;

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public Address(@JsonProperty("address1") final String address1, @JsonProperty("address2") final String address2,
                   @JsonProperty("city") final String city, @JsonProperty("state") final String state,
                   @JsonProperty("zip") final String zip) {
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
}
