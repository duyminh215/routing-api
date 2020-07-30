package com.duyminh215;

import com.graphhopper.*;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.Profile;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.PointList;

import java.util.Locale;

public class Main {

    final static String osmFile = "resources/vietnam-latest.osm.pbf";
    private static final String graphFolder = "resources/map";
    private static GraphHopper hopper;
    public static void main(String[] args){
        System.out.println("test");
        // create one GraphHopper instance
        hopper = new GraphHopperOSM().forServer();
        hopper.setDataReaderFile(osmFile);
        // where to store graphhopper files?
        hopper.setGraphHopperLocation(graphFolder);
        hopper.setEncodingManager(EncodingManager.create("car"));

        // see docs/core/profiles.md to learn more about profiles
        hopper.setProfiles(
                new Profile("car").setVehicle("car").setWeighting("fastest")
        );
        // this enables speed mode for the profile we call "car" here
        hopper.getCHPreparationHandler().setCHProfiles(
                new CHProfile("car")
        );

        // now this can take minutes if it imports or a few seconds for loading
        // of course this is dependent on the area you import
        hopper.importOrLoad();
//        GHResponse ph = graphHopper.route(new GHRequest(21.013268, 105.812856, 21.023323, 105.820055));
        double originLat1 = 21.013268;
        double originLon1 = 105.812856;
        double originLat2 = 21.023323;
        double originLon2 = 105.820055;
        long startTime = System.currentTimeMillis();
        System.out.println("Start time = " + startTime);
        for(int i = 0; i < 1000; i++){
            double la1 = generateRandomLatitude(originLat1);
            double lo1 = generateRandomLongitude(originLon1);
            double la2 = generateRandomLatitude(originLat2);
            double lo2 = generateRandomLongitude(originLon2);
            calculateDistance(la1, lo1, la2, lo2);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("End time = " + endTime);
        System.out.println("Duration = " + (endTime - startTime));
    }

    public static void calculateDistance(double la1, double lo1, double la2, double lo2){
        GHRequest req = new GHRequest(la1, lo1, la2, lo2).
                        setProfile("car").
                        setLocale(Locale.US);
        GHResponse rsp = hopper.route(req);
        if(rsp.hasErrors()) {
            System.out.println("Error = "+rsp.getErrors());
            return;
        }
        ResponsePath path = rsp.getBest();
        PointList pointList = path.getPoints();
        double distance = path.getDistance();
        long timeInMs = path.getTime();
        System.out.println("distance = "+distance);
    }

    public static double generateRandomLatitude(double originLatitude){
        double randomNumber = (Math.random() * ((20000 - 1) + 1)) + 1;
        randomNumber = randomNumber/1000/111;
        return originLatitude + randomNumber;
    }

    public static double generateRandomLongitude(double originLongitude){
        double randomNumber = (Math.random() * ((20000 - 1) + 1)) + 1;
        randomNumber = randomNumber/1000/111;
        return originLongitude + randomNumber;
    }
}
