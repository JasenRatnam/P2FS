package Requests;

import java.io.Serial;

/**
 * class for request from client to deregister a client
 */
public class DeRegisterRequest extends Request {

    @Serial
    private static final long serialVersionUID = 1L;
    String clientName;

    /**
     * contstructor a request that ask to deregister a client with a given name
     * @param clientName to deregister
     */
    public DeRegisterRequest(int rid, String clientName) {
        super(Request.RequestType.DE_REGISTER, rid);
        this.clientName = clientName;
    }

    //getter
    public String getClientName() {
        return clientName;
    }

    //string of the request
    @Override
    public String toString() {
        return Request.RequestType.DE_REGISTER + " " + this.getRQNumb() + " " + getClientName();
    }
}
