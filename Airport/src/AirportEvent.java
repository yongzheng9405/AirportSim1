//Yongzheng Zhang

public class AirportEvent extends Event {
    public static final int PLANE_ARRIVES = 0;
    public static final int PLANE_LANDED = 1;
    public static final int PLANE_TAKEOFF = 2;   // introduce another type of event named takeoff
    public static final int PLANE_DEPARTS = 3;

    private Airplane m_airplane;
    private Airport m_destination;

    AirportEvent(double delay, EventHandler handler, int eventType, Airport destinationaiport) {
        super(delay, handler, eventType);
        m_destination = destinationaiport;             // set information about the destination airport
    }

    public void setPlaneInformation(Airplane airplane) {
        m_airplane = airplane;
    }  // delivery of the information of which airplane has the event

    public Airplane getPlaneInformation() {
        return m_airplane;
    } // get the information of the plane having this event

    public void setPlaneDestination(Airport airport) {
        m_destination = airport;
    } // set destination for the event

    public Airport getPlaneDestination() {
        return m_destination;  // get the information of the destination airport
    }

}
