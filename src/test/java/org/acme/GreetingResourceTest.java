package org.acme;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.acme.service.Location;
import org.acme.service.Vehicle;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given().when().get("/vehicle").then().statusCode(200).body(is("Hello RESTEasy"));
    }

    @Test
    public void testAddVehicleEndpoint() {
        try {
            Random random = new Random();
            Location l = LocationUtils.getLocation(77.580643, 12.972442, 10000);
            String name = RandomStringUtils.randomAlphabetic(5);
            Vehicle v = Vehicle.builder().id(random.nextInt()).name(name).lat(l.getLatitude()).lon(l.getLongitude())
                    .status(new AtomicBoolean(true)).build();
            ObjectMapper s = new ObjectMapper();
            System.out.println(s.writeValueAsString(v));
            given().when().contentType("application/json").body(s.writeValueAsString(v)).post("/vehicle").then()
                    .statusCode(200);
        } catch (Exception e) {
            e.printStackTrace();
        }

        

    }
}