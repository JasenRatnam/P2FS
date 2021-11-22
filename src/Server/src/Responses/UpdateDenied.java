package Responses;

import Requests.Request;

import java.io.Serial;

public class UpdateDenied extends Request {

    @Serial
    private static final long serialVersionUID = 1L;
    private String clientName;
    private String reason;

    public UpdateDenied(int reqNumber, String clientName, String reason) {
        super(RequestType.UPDATE_DENIED, reqNumber);
        this.clientName = clientName;
        this.reason = reason;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return RequestType.UPDATE_DENIED+ " "  + this.getRQNumb()+ " "  + this.getClientName() + " '" + getReason() + "'";

    }
}
