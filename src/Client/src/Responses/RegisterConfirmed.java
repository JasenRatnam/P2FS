package Responses;

import Requests.Request;

//import java.io.Serial;

/**
 * class for response from server when the registrations is confirmed
 */
public class RegisterConfirmed extends Request {

    //@Serial
    private static final long serialVersionUID = 1L;

    //constructors
    public RegisterConfirmed(int RQNumb){
        super(Request.RequestType.CLIENT_REGISTER_CONFIRMED, RQNumb);
        this.RQNumb = RQNumb;
    }

    /**
     * Get a string of the request
     */
    @Override
    public String toString(){
        return Request.RequestType.CLIENT_REGISTER_CONFIRMED+ " " + this.getRQNumb()+ " " ;
    }
}
