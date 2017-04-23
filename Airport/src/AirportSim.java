//Yongzheng Zhang

import java.util.TreeSet;


public class AirportSim {
    public static void main(String[] args) {
        int airplaneNumber = 100;
        int airportNumber = 5;
        double[][] distances = {{0, 2530, 280, 411, 2375},
                                {2530, 0, 2400, 2322, 762},
                                {280, 2400, 0, 133, 1519},
                                {411, 2322, 133, 0, 2061},
                                {2375, 762, 1519, 2061, 0}}; // distance between five airports



        double[][] flightTimeArray = new double[5][5];
        double circlingTime = 0.0;
        Airplane[] airplaneArray = new Airplane[airplaneNumber];
        AirportEvent[] arriveArray = new AirportEvent[airplaneNumber];


        for (int i = 0; i < airplaneNumber; i++) {
            // initialize three airplanes
            String s = String.valueOf(i);
            airplaneArray[i] = new Airplane("air_" + s, i,0, 1, 0.0);
        }


        // get information about five airports
        Airport_array airport_array = new Airport_array();
        Airport[] AirportsMatrix = airport_array.airports;


        // set the flight time for each airport
        for (int i = 0; i < airportNumber; i++) {
            for (int j = 0; j < airportNumber; j++) {
                flightTimeArray[i][j] = distances[i][j] / airplaneArray[0].getSpeed();
            }
        }



        // initialize arriving events to the first airport for each plane
        for (int j = 0; j < airplaneNumber; j++) {
            int initialIndex = AirportsMatrix[0].getInitialAirport();
            arriveArray[j] = new AirportEvent(0, AirportsMatrix[initialIndex], AirportEvent.PLANE_ARRIVES, AirportsMatrix[1]);
            arriveArray[j].setPlaneInformation(airplaneArray[j]);
            Simulator.schedule(arriveArray[j]);
        }

        // set the total timestamp as 20000
        Simulator.stopAt(200000);
        Simulator.run();

        // sum up total circling time for five airports
        for (int j = 0; j < AirportsMatrix.length; j++){
            //circlingTime = circlingTime + airports[j].getCirclingTime();
            System.out.println("Total circling time is: " + AirportsMatrix[j].getCirclingTime() + " from airport " + AirportsMatrix[j].getName());
            System.out.println("Average circling time is: " + AirportsMatrix[j].getCirclingTime()/ (double)AirportsMatrix[j].getAirplaneNumber() + " from airport " + AirportsMatrix[j].getName());
            System.out.println("Total arriving passenger is: " + AirportsMatrix[j].getArrivePassenger() + " from airport " + AirportsMatrix[j].getName());
            System.out.println("Total departing passenger is: " + AirportsMatrix[j].getDepartPassenger() + " from airport " + AirportsMatrix[j].getName());
            //System.out.println(airports[j].getCirclingTime());
            System.out.println("The average circling time for each airplane in " + AirportsMatrix[j].getName() + " is : " + AirportsMatrix[j].getCirclingTime()/ (double)AirportsMatrix[j].getAirplaneNumber());
            System.out.println("The total number of airplanes that arrivied in "+ AirportsMatrix[j].getName() + " is : " + AirportsMatrix[j].getAirplaneNumber());
            //System.out.println(airports[j].getArrivePassenger());
            //System.out.println(airports[j].getDepartPassenger());

        }
        //System.out.println("circling time is: " + circlingTime);
    }
}
