package org.acme;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Ordering;

import org.acme.service.Location;
import org.acme.service.Vehicle;
import org.acme.service.VehicleService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
// @TestHTTPEndpoint(GreetingResource.class)
public class VehicleServiceTest {

    @Inject
    VehicleService vehicleService;

    // @AfterAll
    // public void afterEach(){
    // vehicleService.shutdown();
    // }

    @Test
    public void setup() {
        vehicleService.clear();
        // Genarte random locations for vehicles
        IntStream.range(0, 50).parallel().forEach(k -> {
            Random random = new Random();
            Location l = LocationUtils.getLocation(77.580643, 12.972442, 10000);
            String name = RandomStringUtils.randomAlphabetic(5);
            Vehicle v = Vehicle.builder().id(random.nextInt()).name(name).lat(l.getLatitude()).lon(l.getLongitude())
                    .status(new AtomicBoolean(true)).build();
            vehicleService.addVehicle(l.getLongitude(), l.getLatitude(), v);
        });
    }

    @Test
    public void testCount() throws JsonProcessingException {

        assertTrue(vehicleService.count() == 50, "There are 50 vehicles in 10 kms radius");

        // given().when().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        // .header(HttpHeaders.ACCEPT,
        // MediaType.APPLICATION_JSON).body(m.writeValueAsString(v)).post().then()
        // .statusCode(201);
    }

    @Test
    public void testSearch() {
        Map<Integer, Double> nearMeSet = vehicleService.searchVehicles(5L, 77.580643, 12.972442, 5000);

        assertTrue(nearMeSet.size() != 0, "There are soem vehicles near you. book them soon");
        assertTrue(Ordering.natural().isOrdered(nearMeSet.values()), "There are vehicles sorted by distance ");

    }

    @Test
    public void testBook() {
        boolean couldBook = vehicleService.book(5L, 77.580643, 12.972442, 5000);
        assertTrue(couldBook, "Only 1 thread and could easily book");
    }

    @Test
    public void testBookWith10ConcurrentThreads() {
        // Creat threads and see that no two people book the same vehcile
    }

    @Test
    public void testPaymentExpiredOrTimeout() {
        vehicleService.paymentExpiredOrTimeout(5L);
    }

}
