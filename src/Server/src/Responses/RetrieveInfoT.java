package Responses;

import Requests.Request;

import java.io.Serial;
import java.util.ArrayList;

public class RetrieveInfoT extends Request {

    @Serial
    private static final long serialVersionUID = 1L;
    private String clientName;
    private String address;
    private int TCPport;
    private ArrayList<String> listOfFiles;

    public RetrieveInfoT(int reqNumber, String clientName, String address, int TCPport, ArrayList<String> listOfFiles) {
        super(RequestType.RETRIEVE_INFOT_RESPONSE, reqNumber);
        this.clientName = clientName;
        this.address = address;
        this.TCPport = TCPport;
        this.listOfFiles = listOfFiles;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTCPport() {
        return TCPport;
    }

    public void setTCPport(int TCPport) {
        this.TCPport = TCPport;
    }

    public ArrayList<String> getListOfFiles() {
        return listOfFiles;
    }

    public void setListOfFiles(ArrayList<String> listOfFiles) {
        this.listOfFiles = listOfFiles;
    }


    @Override
    public String toString() {
        return RequestType.RETRIEVE_INFOT_RESPONSE + " " + this.getRQNumb() + " "
                + getClientName() + " " +
                getAddress() + " "+ getTCPport()+ " "+
                getListOfFiles();
    }
}
