import java.io.Serial;

public class RegisterRequest extends Request  {

    @Serial
    private static final long serialVersionUID = 1L;
    private String address;
    private int UDPport;
    private int TCPport;
    private String clientName;

    /**
     * contstructor a request that ask to register a client
     * @param rqNumber the id number of the request
     * @param clientName name of client
     * @param address ip address of client
     * @param UDPport UDP port of client
     * @param TCPport TCP port of client
     */
    public RegisterRequest(int rqNumber, String clientName, String address, int UDPport, int TCPport) {
        super(RequestType.REGISTER, rqNumber);
        this.clientName = clientName;
        this.address = address;
        this.UDPport = UDPport;
        this.TCPport = TCPport;
    }

    //getter methods

    public String getClientName() {
        return clientName;
    }

    public String getAddress() {
        return address;
    }

    public int getUDPPort() {
        return UDPport;
    }
    public int getTCPPort() {
        return TCPport;
    }

    //print the request
    @Override
    public String toString() {
        return RequestType.REGISTER + " " + this.getRQNumb() + " " + getClientName() + " " + getAddress() + " UDP: "
                + getUDPPort() + " TCP: " + getTCPPort();
    }

    public ClientObject getClientObject() {
        ClientObject client = new ClientObject(clientName,address,UDPport,TCPport);
        return  client;
    }
}