public class ClientObject {

    private String name;
    private String IP;
    private int UDPport;
    private int TCPport;

    /**
     * constructor of a client object
     * @param name of client
     * @param IP of client
     * @param UDPport of client to talk to server
     * @param TCPport of client to talk with other clients
     */
    public ClientObject(String name, String IP, int UDPport, int TCPport) {
        this.name = name;
        this.IP = IP;
        this.UDPport = UDPport;
        this.TCPport = TCPport;
    }

    /**
     * String of the client
     * @return
     */
    @Override
    public String toString() {
        return "ClientObject{" +
                "name='" + name + '\'' +
                ", IP='" + IP + '\'' +
                ", UDPport=" + UDPport +
                ", TCPport=" + TCPport +
                '}';
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getUDPport() {
        return UDPport;
    }

    public void setUDPport(int UDPport) {
        this.UDPport = UDPport;
    }

    public int getTCPport() {
        return TCPport;
    }

    public void setTCPport(int TCPport) {
        this.TCPport = TCPport;
    }
}
