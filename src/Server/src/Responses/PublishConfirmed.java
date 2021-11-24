package Responses;

import Requests.Request;

//import java.io.Serial;

public class PublishConfirmed extends Request {

    //@Serial
    private static final long serialVersionUID = 1L;

    //constructor
    public PublishConfirmed(int RQNumb){
        super(RequestType.PUBLISHED);
        this.RQNumb = RQNumb;
    }

    /**
     * Get a string of the request
     */
    @Override
    public String toString(){
        return RequestType.PUBLISHED+ " " + this.getRQNumb()+ " " ;
    }


}
