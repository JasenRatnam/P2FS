package Responses;

import Requests.Request;

//import java.io.Serial;

public class DownloadError extends Request {

    //@Serial
    private static final long serialVersionUID = 1L;
    private String reason;

    /**
     * contstructor a response for a download error
     *
     */
    public DownloadError(int rqNumber, String reason) {
        super(Request.RequestType.DOWNLOAD_ERROR, rqNumber);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    //print the response
    @Override
    public String toString() {
        return Request.RequestType.DOWNLOAD_ERROR + " " + this.getRQNumb() + " " + getReason();
    }
}
