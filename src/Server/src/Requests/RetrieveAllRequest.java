package Requests;

import java.io.Serial;
import java.util.ArrayList;

public class RetrieveAllRequest extends Request{

    @Serial
    private static final long serialVersionUID = 1L;

    public RetrieveAllRequest(RequestType requestType, int reqNumber) {
        super(requestType, reqNumber);
    }

    @Override
    public String toString() {
        return RequestType.RETRIEVE_ALL + " " + this.getRQNumb();
    }
}
