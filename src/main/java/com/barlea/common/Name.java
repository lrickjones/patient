package com.barlea.common;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType
public class Name {

    @Property()
    private final String first;

    @Property()
    private final String middle;

    @Property()
    private final String last;

    public String getFirst() {
        return first;
    }

    public String getMiddle() {
        return middle;
    }

    public String getLast() {
        return last;
    }

    public Name() {
        this.first = "";
        this.middle = "";
        this.last = "";
    }

    public Name(@JsonProperty("first") final String first, @JsonProperty("middle") final String middle,
                @JsonProperty("last") final String last) {
        this.first = first;
        this.middle = middle;
        this.last = last;
    }
}
