package Responses;

import Handler.ClientObject;
import Requests.Request;

import java.io.Serial;
import java.util.ArrayList;

public class SearchFileResponse extends Request {

    @Serial
    private static final long serialVersionUID = 1L;
    private ArrayList<ClientObject> clients;

    public SearchFileResponse(RequestType requestType, int reqNumber, ArrayList<ClientObject> clients) {
        super(requestType, reqNumber);
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
        return RequestType.SEARCH_FILE_RESPONSE + " " + this.getRQNumb()+ " " + this.getClients();
    }
}
