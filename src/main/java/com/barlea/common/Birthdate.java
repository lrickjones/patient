package com.barlea.common;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.time.LocalDate;

@DataType
public class Birthdate {
    @Property()
    private final int year;

    @Property()
    private final int month;

    @Property()
    private final int day;

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public LocalDate asLocalDate() {
        return LocalDate.of(this.year,this.month,this.day);
    }

    public Birthdate(@JsonProperty("year") final int year, @JsonProperty("month") final int month,
                @JsonProperty("day") final int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
