package Requests;

import java.io.Serial;

public class RetrieveAllRequest extends Request{

    @Serial
    private static final long serialVersionUID = 1L;

    public RetrieveAllRequest(int reqNumber) {
        super(RequestType.RETRIEVE_ALL, reqNumber);
    }

    @Override
    public String toString() {
        return RequestType.RETRIEVE_ALL + " " + this.getRQNumb();
    }
}
