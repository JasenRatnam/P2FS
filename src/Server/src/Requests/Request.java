package Requests;

import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    protected RequestType requestType;
    protected int RQNumb;

    /**
     * all the type of possible requests and responses
     */
    public enum RequestType {
        REGISTER("REGISTER"),
        CLIENT_REGISTER_CONFIRMED("REGISTERED"),
        CLIENT_REGISTER_DENIED("REGISTER-DENIED"),
        DE_REGISTER("DE-REGISTER"),
        UPDATE_CONTACT("UPDATE-CONTACT"),
        UPDATE_CONFIRMED("UPDATE-CONFIRMED"),
        UPDATE_DENIED("UPDATE-DENIED"),
        PUBLISH("PUBLISH"),
        PUBLISHED("PUBLISHED"),
        PUBLISH_DENIED("PUBLISH-DENIED"),
        REMOVE("REMOVE"),
        REMOVED("REMOVED"),
        REMOVE_DENIED("REMOVE-DENIED"),
        RETRIEVE_ALL ("RETRIEVE-ALL"),
        RETRIEVE ("RETRIEVE"),
        RETRIEVE_INFOT ("RETRIEVE-INFOT"),
        RETRIEVE_INFOT_RESPONSE ("RETRIEVE-INFOT"),
        RETRIEVE_ERROR ("RETRIEVE-ERROR"),
        SEARCH_FILE ("SEARCH-FILE"),
        SEARCH_FILE_RESPONSE ("SEARCH-FILE"),
        SEARCH_ERROR ("SEARCH-ERROR"),
        DOWNLOAD ("DOWNLOAD"),
        FILE ("FILE"),
        FILE_END ("FILE-END"),
        DOWNLOAD_ERROR ("DOWNLOAD-ERROR");

        private final String requestType;
        //constructor of request
        RequestType(String requestType){
            this.requestType = requestType;
        }

        /**
         * get string of the request type
         */
        public String toString(){
            return this.requestType;
        }
    }

    /**
     * Requests.Request constructor without request number
     */
    public Request(RequestType requestType) {
        this.requestType = requestType;
    }

    /**
     * Requests.Request constructor with request number
     */
    public Request(RequestType requestType, int reqNumber) {
        this.requestType = requestType;
        this.RQNumb = reqNumber;
    }

    // getters and setters
    public int getRQNumb(){
        return RQNumb;
    }
}