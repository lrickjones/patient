package com.barlea.hipaa.chaincode;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContextFactory;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

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
                    .hasMessage("Patient PTNT000 does not exist");
        }
/*
        // Sample code I thought looked interesting to try later

        import static org.hamcrest.Matchers.equalTo;
        import static org.hamcrest.Matchers.is;
        import static org.hamcrest.Matchers.sameInstance;
        import static org.junit.Assert.assertThat;

        @Test
        public void getInstance() {
            final ContextFactory f1 = ContextFactory.getInstance();
            final ContextFactory f2 = ContextFactory.getInstance();
            assertThat(f1, sameInstance(f2));
        }

        @Test
        public void createContext() {
            final ChaincodeStub stub = new ChaincodeStubNaiveImpl();
            final Context ctx = ContextFactory.getInstance().createContext(stub);

            assertThat(stub.getArgs(), is(equalTo(ctx.getStub().getArgs())));
            assertThat(stub.getStringArgs(), is(equalTo(ctx.getStub().getStringArgs())));
            assertThat(stub.getFunction(), is(equalTo(ctx.getStub().getFunction())));
            assertThat(stub.getParameters(), is(equalTo(ctx.getStub().getParameters())));
            assertThat(stub.getTxId(), is(equalTo(ctx.getStub().getTxId())));
            assertThat(stub.getChannelId(), is(equalTo(ctx.getStub().getChannelId())));
            assertThat(stub.invokeChaincode("cc", Collections.emptyList(), "ch0"),
                    is(equalTo(ctx.getStub().invokeChaincode("cc", Collections.emptyList(), "ch0"))));

            assertThat(stub.getState("a"), is(equalTo(ctx.getStub().getState("a"))));
            ctx.getStub().putState("b", "sdfg".getBytes());
            assertThat(stub.getStringState("b"), is(equalTo(ctx.getStub().getStringState("b"))));

            assertThat(ctx.clientIdentity.getMSPID(), is(equalTo("testMSPID")));
            assertThat(ctx.clientIdentity.getId(), is(equalTo(
                    "x509::CN=admin, OU=Fabric, O=Hyperledger, ST=North Carolina,"
                            + " C=US::CN=example.com, OU=WWW, O=Internet Widgets, L=San Francisco, ST=California, C=US")));
        }
*/
    }

}
