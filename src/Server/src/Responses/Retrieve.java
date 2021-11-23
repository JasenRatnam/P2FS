package Responses;

import Handler.ClientObject;
import Requests.Request;

import java.io.Serial;
import java.util.ArrayList;

public class Retrieve extends Request{

    @Serial
    private static final long serialVersionUID = 1L;
    private ArrayList<ClientObject> clients;

    public Retrieve(int reqNumber, ArrayList<ClientObject> clients) {
        super(RequestType.RETRIEVE, reqNumber);
        this.clients = clients;
    }

    public ArrayList<ClientObject> getClients() {
        return clients;
    }


    public void setClients(ArrayList<ClientObject> clients) {
        this.clients = clients;
    }

    @Override
    public String toString() {
     return   Request.RequestType.RETRIEVE + " " + this.getRQNumb() + " " + this.getClients();
    }


}
