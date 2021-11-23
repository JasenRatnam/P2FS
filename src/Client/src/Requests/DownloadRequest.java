package Requests;

import java.io.Serial;

public class DownloadRequest extends Request {

    @Serial
    private static final long serialVersionUID = 1L;
    private String fileName;

    /**
     * contstructor a request that ask to download a file from another client
     *
     */
    public DownloadRequest(int rqNumber, String fileName) {
        super(Request.RequestType.DOWNLOAD, rqNumber);
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
        return Request.RequestType.DOWNLOAD + " " + this.getRQNumb() + " " + getFileName();
    }
}
