//Yongzheng Zhang
import java.util.Queue;
import java.util.LinkedList;


public class Airport implements EventHandler   {

    //TODO add landing and takeoff queues, random variables

    private int m_inTheAir;
    private int m_onTheGround;
    private int m_airport_index;
    private int m_passengerArrive;
    private int m_passengerDepart;
    private int m_numberOfPlanes;

    private boolean m_freeToLand;


    private double m_flightTime;
    private double m_runwayTimeToLand;
    private double m_requiredTimeOnGround;
    private double m_takeoffTime;
    private double m_circlingTime;

    private String m_airportName;

    Queue<AirportEvent> arrivalQueue = new LinkedList<AirportEvent>(); // generate a first in first out queue for arriving planes to land
    Queue<AirportEvent> takeoffQueue = new LinkedList<AirportEvent>(); // generate a first in first out queue for landed planes to take off


    private AirportEvent airport_event;
    public Airport (){

    }


    public Airport(String name, double runwayTimeToLand, double requiredTimeOnGround, double takeoffTime, double flightTime, int airport_index) {
        m_airportName = name;
        m_inTheAir = 0;
        m_onTheGround = 0;
        m_passengerArrive = 0;
        m_passengerDepart = 0;
        m_circlingTime = 0.0;
        m_freeToLand = true;
        m_runwayTimeToLand = runwayTimeToLand;
        m_requiredTimeOnGround = requiredTimeOnGround;
        m_takeoffTime = takeoffTime;
        m_flightTime = flightTime;
        m_airport_index = airport_index;
        m_numberOfPlanes = 0;
    }

    public String getName() {
        return m_airportName;
    }

    public int getIndex() { return m_airport_index; }

    public double getCirclingTime() {return m_circlingTime;}

    public void setFlightTime(double flighttime) {
        m_flightTime = flighttime;
    }

    public double getM_flightTime() {
        return m_flightTime;
    }

    public int getArrivePassenger() {return m_passengerArrive;}

    public int getDepartPassenger() {return m_passengerDepart;}

    public int getAirplaneNumber() {return m_numberOfPlanes;}

    public int setDestinationIndex() {
        int randomNumber = (int) (Math.random() * 5);
        while (randomNumber == m_airport_index) {
            randomNumber = (int) (Math.random() * 5);
        }
        return randomNumber;
    }

    public int getInitialAirport() {
        return (int) (Math.random() * 5);
    }



    public void handle(Event event) {
        //System.out.println("m_arrive at airport " + m_airportName + " : " + this.m_passenger_arrive);
        AirportEvent airEvent = (AirportEvent)event;
        Airplane airplane1 = airEvent.getPlaneInformation();

        Airport_array airport_array = new Airport_array();
        Airport[] airports = airport_array.airports;   // get information for five airports

        switch(airEvent.getType()) {
            case AirportEvent.PLANE_ARRIVES:
                m_inTheAir++;
                m_numberOfPlanes++;
                System.out.println(Simulator.getCurrentTime() + ": Plane " + airplane1.getName() + " arrived at airport " + m_airportName);
                airplane1.setArrivalTime(Simulator.getCurrentTime());  // store the arrival time for the plane

                /* if there is no plane waiting to land and the runway is free, schedule a landed event for this plane
                   otherwise push the event into arrival_Queue */
                if (arrivalQueue.isEmpty() == true) {
                    if(m_freeToLand) {
                        AirportEvent landedEvent = new AirportEvent(m_runwayTimeToLand, this, AirportEvent.PLANE_LANDED, airEvent.getPlaneDestination());
                        landedEvent.setPlaneInformation(airplane1);
                        Simulator.schedule(landedEvent);
                        m_freeToLand = false;
                        m_circlingTime = m_circlingTime + Simulator.getCurrentTime() - airplane1.getArrivalTime();
                    }
                    else {
                        arrivalQueue.offer(airEvent);
                    }
                }
                else {
                    arrivalQueue.offer(airEvent);
                }

                break;



            case AirportEvent.PLANE_LANDED:
                m_inTheAir--;
                m_onTheGround++;
                this.m_passengerArrive = this.m_passengerArrive + airplane1.getPassengerNumber(); // calculate the number of passengers arriving that airport
                System.out.println(Simulator.getCurrentTime() + ": Plane " + airplane1.getName() + " lands at airport " + m_airportName);
                //System.out.println("Total arriving passenger is: " + this.m_passenger_arrive + " at airport " + m_airportName);

                // schedule a takeoff event for this airplane
                AirportEvent takeoffEvent = new AirportEvent(m_requiredTimeOnGround, this, AirportEvent.PLANE_TAKEOFF, airEvent.getPlaneDestination());
                takeoffEvent.setPlaneInformation(airplane1);
                Simulator.schedule(takeoffEvent);

                // if there is no airplane waiting to land, schedule an airplane to take off
                if (arrivalQueue.isEmpty() == true && takeoffQueue.isEmpty() == true) {
                m_freeToLand = true;
                }
                else if (arrivalQueue.isEmpty() == true && takeoffQueue.isEmpty() == false) {
                    AirportEvent bEvent = takeoffQueue.poll();
                    AirportEvent departEvent = new AirportEvent(m_takeoffTime, this, AirportEvent.PLANE_DEPARTS, bEvent.getPlaneDestination());
                    departEvent.setPlaneInformation(bEvent.getPlaneInformation());
                    Simulator.schedule(departEvent);
                }
                // if there is no airplane waiting to take off, schedule an airplane to land
                else if (arrivalQueue.isEmpty() == false && takeoffQueue.isEmpty() == true) {
                    AirportEvent aEvent = arrivalQueue.poll();
                    AirportEvent landedEvent = new AirportEvent(m_runwayTimeToLand, this, AirportEvent.PLANE_LANDED, aEvent.getPlaneDestination());
                    landedEvent.setPlaneInformation(aEvent.getPlaneInformation());
                    Simulator.schedule(landedEvent);
                    m_circlingTime = m_circlingTime + Simulator.getCurrentTime() - aEvent.getPlaneInformation().getArrivalTime();
                }

                // if there are airplanes waiting to land and take off, compare their timestamps
                else {
                    if (arrivalQueue.element().getTime() < takeoffQueue.element().getTime()) {
                        AirportEvent aEvent1 = arrivalQueue.poll();
                        AirportEvent landedEvent1 = new AirportEvent(m_runwayTimeToLand, this, AirportEvent.PLANE_LANDED, aEvent1.getPlaneDestination());
                        landedEvent1.setPlaneInformation(aEvent1.getPlaneInformation());
                        Simulator.schedule(landedEvent1);
                        m_circlingTime = m_circlingTime + Simulator.getCurrentTime() - aEvent1.getPlaneInformation().getArrivalTime();
                    }
                    else {
                        AirportEvent bEvent1 = takeoffQueue.poll();
                        AirportEvent departEvent1 = new AirportEvent(m_takeoffTime, this, AirportEvent.PLANE_DEPARTS, bEvent1.getPlaneDestination());
                        departEvent1.setPlaneInformation(bEvent1.getPlaneInformation());
                        Simulator.schedule(departEvent1);
                    }
                }

                break;



            case AirportEvent.PLANE_TAKEOFF:
                System.out.println(Simulator.getCurrentTime() + ": Plane " + airplane1.getName() + " takes off from airport " + m_airportName);

                /* if there is no plane waiting to take off and the runway is free, schedule a departure event for this plane
                   otherwise push the event into takeoff_Queue */
                if (takeoffQueue.isEmpty() == true) {
                    if(m_freeToLand) {
                        AirportEvent departEvent = new AirportEvent(m_takeoffTime, this, AirportEvent.PLANE_DEPARTS, airEvent.getPlaneDestination());
                        departEvent.setPlaneInformation(airplane1);
                        Simulator.schedule(departEvent);
                        m_freeToLand = false;
                    }
                    else {
                        takeoffQueue.offer(airEvent);
                    }
                }
                else {
                    takeoffQueue.offer(airEvent);
                }

                break;




            case AirportEvent.PLANE_DEPARTS:
                m_onTheGround--;
                airplane1.generatePassengerNumber();
                this.m_passengerDepart = this.m_passengerDepart + airplane1.getPassengerNumber();
                System.out.println(Simulator.getCurrentTime() + ": Plane " + airplane1.getName() + " departs from airport " + m_airportName);
                //System.out.println("Total departing passenger is: " + this.m_passenger_depart + " at airport " + m_airportName);

                AirportEvent arrivalEvent;
                int newDestination = this.setDestinationIndex();


                this.setFlightTime(airport_array.distances[m_airport_index][newDestination]/airplane1.getSpeed());

                arrivalEvent = new AirportEvent(m_flightTime, this, AirportEvent.PLANE_ARRIVES, airports[newDestination]);
                arrivalEvent.setHandler(airports[newDestination]);

                arrivalEvent.setPlaneInformation(airplane1);
                Simulator.schedule(arrivalEvent);



                if (arrivalQueue.isEmpty() == true && takeoffQueue.isEmpty() == false) {
                    AirportEvent bEvent = takeoffQueue.poll();
                    AirportEvent departEvent = new AirportEvent(m_takeoffTime, this, AirportEvent.PLANE_DEPARTS, bEvent.getPlaneDestination());
                    departEvent.setPlaneInformation(bEvent.getPlaneInformation());
                    Simulator.schedule(departEvent);
                }
                else if (arrivalQueue.isEmpty() == false && takeoffQueue.isEmpty() == true) {
                    AirportEvent aEvent = arrivalQueue.poll();
                    AirportEvent landedEvent = new AirportEvent(m_runwayTimeToLand, this, AirportEvent.PLANE_LANDED, aEvent.getPlaneDestination());
                    landedEvent.setPlaneInformation(aEvent.getPlaneInformation());
                    Simulator.schedule(landedEvent);
                    m_circlingTime = m_circlingTime + Simulator.getCurrentTime() - aEvent.getPlaneInformation().getArrivalTime();
                }
                else if (arrivalQueue.isEmpty() == true && takeoffQueue.isEmpty() == true) {
                    m_freeToLand = true;
                }
                else {
                    if (arrivalQueue.element().getTime() < takeoffQueue.element().getTime()) {
                        AirportEvent aEvent1 = arrivalQueue.poll();
                        AirportEvent landedEvent1 = new AirportEvent(m_runwayTimeToLand, this, AirportEvent.PLANE_LANDED, aEvent1.getPlaneDestination());
                        landedEvent1.setPlaneInformation(aEvent1.getPlaneInformation());
                        Simulator.schedule(landedEvent1);
                        m_circlingTime = m_circlingTime + Simulator.getCurrentTime() - aEvent1.getPlaneInformation().getArrivalTime();
                    }
                    else {
                        AirportEvent bEvent1 = takeoffQueue.poll();
                        AirportEvent departEvent1 = new AirportEvent(m_takeoffTime, this, AirportEvent.PLANE_DEPARTS, bEvent1.getPlaneDestination());
                        departEvent1.setPlaneInformation(bEvent1.getPlaneInformation());
                        Simulator.schedule(departEvent1);
                    }
                }
                break;

        }
    }
}
