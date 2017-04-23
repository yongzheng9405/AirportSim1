//Yongzheng Zhang

//TODO add number of passengers, speed

public class Airplane {
    public static final double m_speed = 10.0;    // the speed is about 10.0 miles per minute
    public static final int m_maxPassengers = 550;

    private String m_name;
    private int m_numberPassengers;
    private int m_plane_index;
    private int m_current_index;
    private int m_destination_index;
    private double m_arrivalTime;



    public Airplane(String name, int plane_index, int current_index, int destination_index, double arrivaltime) {

        m_name = name;
        m_current_index = current_index;
        m_destination_index = destination_index;
        m_plane_index = plane_index;
        m_arrivalTime = arrivaltime;
        generatePassengerNumber();   // initialize random number of passenger on the plane
        //System.out.println("initial passenger: " + m_numberPassengers);

    }

    public String getName() {
        return m_name;
    }

    // generate a random number between 300 and 700 each time when a plane departs
    public void generatePassengerNumber() {
        m_numberPassengers = 200 + (int)(Math.random() * ((m_maxPassengers - 200) + 1));
    }

    public double getSpeed() {
        return m_speed;
    }

    public int getPassengerNumber() {return m_numberPassengers;}


    public int getTarget() {return m_destination_index;}

    // get the arrival time for the plane for future use of circling time calculation
    public double getArrivalTime() {return m_arrivalTime;}

    public void setArrivalTime(double arrivaltime) {
        m_arrivalTime = arrivaltime;
    }


}
