package Responses;

import Requests.Request;

//import java.io.Serial;

/**
 * class for response from server when the registrations is denied
 */
public class RegisterDenied extends Request {

    //@Serial
    private static final long serialVersionUID = 1L;
    private String reason;

    /**
     * constructor of a denied register
     */
    public RegisterDenied(String reason, int rid){
      super(Request.RequestType.CLIENT_REGISTER_DENIED, rid);
      this.RQNumb = rid;
      this.reason=reason;
    }

    //getter
    public String getReason() {
        return reason;
    }

    //string of the response
    @Override
    public String toString(){
        return Request.RequestType.CLIENT_REGISTER_DENIED+ " "  + this.getRQNumb()+ " '" + getReason() + "'";
    }
}

