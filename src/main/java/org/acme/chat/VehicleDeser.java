package org.acme.chat;

import org.acme.service.Vehicle;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class VehicleDeser extends ObjectMapperDeserializer<Vehicle> {
    public VehicleDeser(){
        // pass the class to the parent.
        super(Vehicle.class);
    }
}