package org.acme;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.service.Vehicle;
import org.acme.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.common.annotation.Blocking;

@Path("/vehicle")
@Blocking
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GreetingResource {

    private static final Logger log = LoggerFactory.getLogger("greeting");

    @Inject
    VehicleService vehicleService;

    @GET
    public String hello() {
       return "Hello RESTEasy";
    }


    @POST
    public Vehicle add(Vehicle vehicle) {
       return vehicleService.addVehicle(vehicle.getLon(), vehicle.getLat(), vehicle);
    }
}