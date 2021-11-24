package Requests;


//import java.io.Serial;
import java.util.ArrayList;

/**
 * class for publish files for a client
 */
public class PublishRequest extends Request{

    //@Serial
    private static final long serialVersionUID = 1L;
    private String clientName;
    private ArrayList<String> listOfFiles;

    /**
     * contstructor a request that ask to register a client
     * @param rqNumber the id number of the request
     * @param clientName name of client
     * @param listOfFiles list of files for the client
     */
    public PublishRequest(int rqNumber, String clientName, ArrayList<String> listOfFiles) {
        super(Request.RequestType.PUBLISH, rqNumber);
        this.clientName = clientName;
        this.listOfFiles = listOfFiles;
    }

    //getter and setters
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public ArrayList<String> getListOfFiles() {
        return listOfFiles;
    }

    public void setListOfFiles(ArrayList<String> listOfFiles) {
        this.listOfFiles = listOfFiles;
    }

    //print the request
    @Override
    public String toString() {
        return Request.RequestType.PUBLISH + " " + this.getRQNumb() + " " + getClientName() + " " + getListOfFiles();
    }
}
