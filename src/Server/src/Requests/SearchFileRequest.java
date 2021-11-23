package Requests;

//import java.io.Serial;

public class SearchFileRequest extends Request{

    //@Serial
    private static final long serialVersionUID = 1L;
    private String filename;


    public SearchFileRequest(int reqNumber, String filename) {
        super(RequestType.SEARCH_FILE, reqNumber);
        this.filename=filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return RequestType.SEARCH_FILE + " " + this.getRQNumb()+ " " + this.getFilename();
    }
}
