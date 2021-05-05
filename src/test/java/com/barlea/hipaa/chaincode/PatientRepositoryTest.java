package com.barlea.hipaa.chaincode;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public final class PatientRepositoryTest {

    @Nested
    class InvokeQueryPatientTransaction {

        @Test
        public void whenAgreementExists() {
            PatientRepository contract = new PatientRepository();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("PTNT000"))
                    .thenReturn("{\"name\": {\"first\":\"Jon\",\"middle\":\"J\",\"last\":\"Doe\"}," +
                                "\"gender\": \"Male\"," +
                                "\"address\": {\"address1\":\"123 Easy St\",\"address2\":\"Basement\"," +
                                    "\"city\":\"Clarkston\", \"state\":\"WA\", \"zip\":\"99403\"}," +
                                "\"birthdate\":{\"year\":\"1965\",\"month\":\"04\",\"day\":\"20\"}}");
            Patient patient = contract.getPatient(ctx, "PTNT000");

            assertThat(patient.getName().getFirst())
                    .isEqualTo("Jon");
            assertThat(patient.getName().getMiddle())
                    .isEqualTo("J");
            assertThat(patient.getName().getLast())
                    .isEqualTo("Doe");
            assertThat(patient.getGender())
                    .isEqualTo("Male");
            //TODO: Add Address
            assertThat(patient.getBirthdate().asLocalDate())
                    .isEqualTo(LocalDate.of(1965,04,20));
        }

        @Test
        public void whenCarDoesNotExist() {
            PatientRepository contract = new PatientRepository();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("PTNT000")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.getPatient(ctx, "PTNT000");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Agreement PTNT000 does not exist");
        }

    }

}
