//Yongzheng Zhang

public class Airport_array extends Airport{

    // Give information for five airports, making them reachable for other classes
    public static final Airport BOS = new Airport_array("BOS", 15.0, 300.0, 15.0, 0, 0);
    public static final Airport PDX = new Airport_array("PDX", 15.0, 300.0, 15.0, 0, 1);
    public static final Airport PHL = new Airport_array("PHL", 15.0, 300.0, 15.0, 0, 2);
    public static final Airport IAD = new Airport_array("IAD", 15.0, 300.0, 15.0, 0, 3);
    public static final Airport LAS = new Airport_array("LAS", 15.0, 300.0, 15.0, 0, 4);

    public final Airport[] airports = {BOS, PDX, PHL, IAD, LAS};

    public static final double[][] distances = {{0, 2530, 280, 411, 2375},
                                               {2530, 0, 2400, 2322, 762},
                                               {280, 2400, 0, 133, 1519},
                                               {411, 2322, 133, 0, 2061},
                                               {2375, 762, 1519, 2061, 0}};


    Airport_array(){

    }

    Airport_array(String name, double runwayTimeToLand, double requiredTimeOnGround, double takeoffTime, double flightTime, int airport_index) {
        super(name, runwayTimeToLand, requiredTimeOnGround, takeoffTime, flightTime, airport_index);
    }

}
