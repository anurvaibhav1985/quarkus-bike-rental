package org.acme;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.context.ApplicationScoped;

import org.acme.service.Location;
import org.acme.service.Vehicle;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.mutiny.Multi;

@ApplicationScoped
public class VehicleLatLongGenerator {

    // private Random random = new Random();

    @Outgoing("vehicle-out")
    public Multi<Vehicle> generate() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(5)).onOverflow().drop().map(tick -> {
            Random random = new Random();
            Location l = LocationUtils.getLocation(77.580643, 12.972442, 10000);
            Vehicle v = Vehicle.builder().id(random.nextInt()).lat(l.getLatitude()).lon(l.getLongitude())
                    .status(new AtomicBoolean(true)).build();
            return v;
        });
    }

    
}
