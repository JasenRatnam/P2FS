package Responses;

import Requests.Request;

//import java.io.Serial;

public class RetrieveError extends Request {


    //@Serial
    private static final long serialVersionUID = 1L;
    String reason;

    public RetrieveError( int reqNumber, String reason) {
        super(RequestType.RETRIEVE_ERROR, reqNumber);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return RequestType.RETRIEVE_ERROR+ " "  + this.getRQNumb()+ " '" + getReason() + "'";
    }
}
