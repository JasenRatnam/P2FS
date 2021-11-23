package Responses;

import Requests.Request;

import java.io.Serial;

public class PublishDenied extends Request {

    @Serial
    private static final long serialVersionUID = 1L;
    String reason;

    /**
     * constructor of a denied Publish
     */
    public PublishDenied(String reason, int rid){
        super(RequestType.PUBLISH_DENIED);
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
        return RequestType.PUBLISH_DENIED+ " "  + this.getRQNumb()+ " '" + this.getReason() + "'";
    }


}
