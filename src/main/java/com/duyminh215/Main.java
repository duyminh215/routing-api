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
    public static void main(String[] args){
        System.out.println("test");
        // create one GraphHopper instance
        GraphHopper hopper = new GraphHopperOSM().forServer();
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
        GHRequest req = new GHRequest(21.013268, 105.812856, 21.023323, 105.820055).
                // note that we have to specify which profile we are using even when there is only one like here
                        setProfile("car").
                        setLocale(Locale.US);
        GHResponse rsp = hopper.route(req);

        // first check for errors
        if(rsp.hasErrors()) {
            // handle them!
            // rsp.getErrors()
            return;
        }

        // use the best path, see the GHResponse class for more possibilities.
        ResponsePath path = rsp.getBest();

        // points, distance in meters and time in millis of the full path
        PointList pointList = path.getPoints();
        double distance = path.getDistance();
        long timeInMs = path.getTime();
        System.out.println("distancce = "+distance);

    }
}
