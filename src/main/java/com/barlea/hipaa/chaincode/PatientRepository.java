package com.barlea.hipaa.chaincode;

import com.barlea.common.Address;
import com.barlea.common.Birthdate;
import com.barlea.common.Name;
import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;


@Contract(
        name = "Agreements",
        info = @Info(
                title = "Agreements contract",
                description = "A java chaincode example",
                version = "0.0.1-SNAPSHOT"))

@Default
public final class PatientRepository implements ContractInterface{
    private final Genson genson = new Genson();

    @Transaction()
    public void initLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        Patient patient = new Patient(new Name("Juanita","T","Buck"),
                                    "Female",
                                    new Address("123 East","","Chicago","IL","60615"),
                                    new Birthdate(1983,05,21));

        String patientState = genson.serialize(patient);
        stub.putStringState("PTNT001", patientState);
    }


    @Transaction()
    public Patient getPatient(final Context ctx, final String key) {
        ChaincodeStub stub = ctx.getStub();
        String patientState = stub.getStringState(key);

        if (patientState.isEmpty()) {
            String errorMessage = String.format("Agreement %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, "Agreement not found");
        }

        Patient patient = genson.deserialize(patientState, Patient.class);

        return patient;
    }

    // sample create parameter
    //'{"Args":["createPatient","PTNT000","{\"first\":\"rick\"}","Male","{\"address1\":\"123 Easy St\",\"address2\":\"Basement\",\"city\":\"Clarkston\", \"state\":\"WA\", \"zip\":\"99403\"}","{\"year\":\"1965\",\"month\":\"04\",\"day\":\"20\"}"]}'

    @Transaction()
    public Patient createPatient(final Context ctx, final String key, final Name name, final String gender,
                                 final Address address, final Birthdate birthdate) {
        ChaincodeStub stub = ctx.getStub();

        String patientState = stub.getStringState(key);
        if (!patientState.isEmpty()) {
            String errorMessage = String.format("Patient %s already exists", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, "Patient already exists");
        }

        Patient patient = new Patient(name,gender,address,birthdate);
        patientState = genson.serialize(patient);
        stub.putStringState(key, patientState);

        return patient;
    }


    @Transaction()
    public Patient changePatientStatus(final Context ctx, final String key, final String newStatus) {
        ChaincodeStub stub = ctx.getStub();

        String patientState = stub.getStringState(key);

        if (patientState.isEmpty()) {
            String errorMessage = String.format("Patient %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, "Patient not found");
        }

        Patient patient = genson.deserialize(patientState, Patient.class);

        Patient newPatient = new Patient(patient.getName(), patient.getGender(), patient.getAddress(),patient.getBirthdate());
        String newAgreementState = genson.serialize(newPatient);
        stub.putStringState(key, newAgreementState);

        return newPatient;
    }

}
