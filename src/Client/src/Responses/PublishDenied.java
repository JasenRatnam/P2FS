package Responses;

import Requests.Request;

import java.io.Serial;

public class PublishDenied extends Request {

    @Serial
    private static final long serialVersionUID = 1L;
    private String reason;

    /**
     * constructor of a denied Publish
     */
    public PublishDenied(String reason, int rid){
        super(Request.RequestType.PUBLISH_DENIED, rid);
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
        return Request.RequestType.PUBLISH_DENIED+ " "  + this.getRQNumb()+ " '" + getReason() + "'";
    }


}
