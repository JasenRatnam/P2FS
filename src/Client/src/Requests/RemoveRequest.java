package Requests;

import java.io.Serial;
import java.util.ArrayList;

public class RemoveRequest extends Request {

    @Serial
    private static final long serialVersionUID = 1L;
    private String clientName;
    private ArrayList<String> listOfFiles;

    public RemoveRequest(int reqNumber, String clientName, ArrayList<String> listOfFiles) {
        super(RequestType.REMOVE, reqNumber);
        this.clientName = clientName;
        this.listOfFiles = listOfFiles;
    }

    public String getClientName() {return clientName;}

    public void setClientName(String clientName) {this.clientName = clientName;}

    public ArrayList<String> getListOfFiles() {return listOfFiles;}

    public void setListOfFiles(ArrayList<String> listOfFiles) {this.listOfFiles = listOfFiles;}

    @Override
    public String toString() {
        return RequestType.REMOVE + " " + this.getRQNumb() + " " + getClientName() + " " + getListOfFiles();
    }
}
