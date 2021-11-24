package Responses;

import Requests.Request;

//import java.io.Serial;

public class RemoveConfirmed extends Request {

    //@Serial
    private static final long serialVersionUID = 1L;

    //constructor
    public RemoveConfirmed(int RQNumb){
        super(Request.RequestType.REMOVED, RQNumb);
        this.RQNumb = RQNumb;
    }

    /**
     * Get a string of the request
     */
    @Override
    public String toString(){
        return Request.RequestType.REMOVED + " " + this.getRQNumb()+ " " ;
    }
}
