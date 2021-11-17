package Requests;

import java.io.Serial;

public class Download extends Request {

    @Serial
    private static final long serialVersionUID = 1L;
    private String fileName;

    /**
     * contstructor a request that ask to download a file from another client
     *
     */
    public Download(int rqNumber, String fileName) {
        super(Request.RequestType.REGISTER, rqNumber);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    //print the request

    @Override
    public String toString() {
        return RequestType.DOWNLOAD + " " + this.getRQNumb() + " " + getFileName();
    }
}
