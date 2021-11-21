package Requests;

import Handler.Client;
import Handler.ClientObject;

import java.io.Serial;
import java.util.ArrayList;

public class RetrieveAllRequest extends Request{

    @Serial
    private static final long serialVersionUID = 1L;
    private ArrayList<ClientObject> listOfClients;

    public RetrieveAllRequest(int reqNumber, ArrayList<ClientObject> listOfClients) {
        super(RequestType.RETRIEVE_ALL, reqNumber);
        this.listOfClients = listOfClients;
    }

    @Override
    public String toString() {
        return RequestType.RETRIEVE_ALL + " " + this.getRQNumb();
    }
}
