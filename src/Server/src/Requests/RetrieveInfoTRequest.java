package Requests;

import java.io.Serial;

public class RetrieveInfoTRequest extends Request {

    @Serial
    private static final long serialVersionUID = 1L;
    private String clientName;

    public RetrieveInfoTRequest(int reqNumber, String clientName) {
        super(RequestType.RETRIEVE_INFOT, reqNumber);
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return RequestType.RETRIEVE_INFOT + " " + this.getRQNumb() + " " + getClientName();
    }
}
