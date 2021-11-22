package Responses;

import Requests.Request;

import java.io.Serial;

public class UpdateConfirmed extends Request {

    @Serial
    private static final long serialVersionUID = 1L;
    private String IPaddress;
    private int UDPport;
    private int TCPport;
    private String clientName;

    public UpdateConfirmed(int reqNumber, String IPaddress, int UDPport, int TCPport, String clientName) {
        super(Request.RequestType.UPDATE_CONFIRMED, reqNumber);
        this.IPaddress = IPaddress;
        this.UDPport = UDPport;
        this.TCPport = TCPport;
        this.clientName = clientName;
    }

    public String getIPaddress() {
        return IPaddress;
    }

    public void setIPaddress(String IPaddress) {
        this.IPaddress = IPaddress;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return Request.RequestType.UPDATE_CONFIRMED + " " + this.getRQNumb() + " " + getClientName() + " " + getIPaddress() + " UDP: "
                + getUDPport() + " TCP: " + getTCPport();
    }
}
