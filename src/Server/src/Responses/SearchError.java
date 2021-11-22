package Responses;

import Requests.Request;

import java.io.Serial;

public class SearchError extends Request {


    @Serial
    private static final long serialVersionUID = 1L;
    String reason;

    public SearchError(int reqNumber, String reason) {
        super(RequestType.SEARCH_ERROR, reqNumber);
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
            return RequestType.SEARCH_ERROR+ " "  + this.getRQNumb()+ " '" + this.getReason() + "'";

    }
}
