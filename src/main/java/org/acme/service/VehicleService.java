package org.acme.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.acme.RedisConfig;
import org.redisson.api.GeoOrder;
import org.redisson.api.GeoUnit;
import org.redisson.api.RGeo;
import org.redisson.api.RMap;
import org.redisson.api.RMapReactive;
import org.redisson.api.geo.GeoSearchArgs;

import io.smallrye.mutiny.Multi;

@ApplicationScoped
public class VehicleService {

    @Inject
    RedisConfig config;

    RGeo<Integer> vehicleSpace;

    RMap<Integer, Vehicle> m;

    RMap<Long, Vehicle> activeBookings;

    public void shutdown(){
        config.get().shutdown();

        // RMapReactive<Integer, Integer> set = config.get().reactive().getMap("test");
        // set.put(1, 1);
        // Multi.createFrom().publisher(set.get(1));
        
    }

    public void clear() {
        vehicleSpace = config.get().getGeo("bike-rentals");
        vehicleSpace.clear();
        m = config.get().getMap("vehiclesDB");
        m.clear();
        activeBookings = config.get().getMap("activeBookings");
        activeBookings.clear();
    }

    public Integer count() {

        return config.get().getGeo("bike-rentals").size();
    }

    // public Vehicle getVehicle(Integer id) {
    // m = config.get().getMap("vehiclesDB");
    // return m.get(id);
    // }

    public Vehicle addVehicle(double longitude, double latitude, Vehicle vehicle) {

        vehicleSpace = config.get().getGeo("bike-rentals");
        vehicleSpace.add(longitude, latitude, vehicle.getId());

        m = config.get().getMap("vehiclesDB");
        m.put(vehicle.getId(), vehicle);
        return vehicle;

    }

    public Map<Integer, Double> searchVehicles(Long userId, double longitude, double latitude, int radius) {

        vehicleSpace = config.get().getGeo("bike-rentals");

        Map<Integer, Double> t = vehicleSpace.searchWithDistance(
                GeoSearchArgs.from(longitude, latitude).radius(radius, GeoUnit.METERS).order(GeoOrder.ASC));
       
        return t;

    }

    public boolean book(Long userId, double longitude, double latitude, int radius) {

        boolean couldBeBooked = false;
        Map<Integer, Double> t = vehicleSpace.searchWithDistance(
            GeoSearchArgs.from(longitude, latitude).radius(radius, GeoUnit.METERS).order(GeoOrder.ASC));
   
        
        List<Integer> top5Nearest = t.keySet().stream().limit(5).collect(Collectors.toList());
        RMap<Integer,Vehicle> k = config.get().getMap("vehiclesDB");

        for (Integer vehicleId : top5Nearest) {
            Vehicle vehicle = k.get(vehicleId);
            couldBeBooked = vehicle.getStatus().compareAndSet(true, false); 
            if (couldBeBooked) {
                activeBookings = config.get().getMap("activeBookings");
                activeBookings.put(userId, vehicle);
                k.put(vehicleId ,vehicle);
                vehicleSpace = config.get().getGeo("bike-rentals");
                //System.out.println(vehicleSpace.addIfExists(vehicle.getLon(),vehicle.getLat(),vehicleId));
                return true;
            }
        }

        return false;
    }

    public void paymentExpiredOrTimeout(long l) {
        activeBookings = config.get().getMap("activeBookings");
        Vehicle v = activeBookings.get(l);
        vehicleSpace = config.get().getGeo("bike-rentals");
        v.getStatus().set(true);
        //GeoEntry n = new GeoEntry(v.getLon(), v.getLat(), v);
        System.out.println(vehicleSpace.addIfExists(v.getLon(), v.getLat(), v.getId()));
    }

}
