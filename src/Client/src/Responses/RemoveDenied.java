package Responses;

import Requests.Request;

import java.io.Serial;

public class RemoveDenied extends Request{

    @Serial
    private static final long serialVersionUID = 1L;
    private String reason;

    /**
     * constructor of a denied Publish
     */
    public RemoveDenied(String reason, int rid){
        super(Request.RequestType.REMOVE_DENIED, rid);
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
        return Request.RequestType.REMOVE_DENIED+ " "  + this.getRQNumb()+ " '" + getReason() + "'";
    }
}
