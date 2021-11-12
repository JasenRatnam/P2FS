import java.io.IOException;

public class RequestList {

    private static String log;

    /**
     * Save request sent by the client to the HashMap
     * @param
     */
    public static void handleSentRequest(Object request) {

        // Handle Register Request
        if (request instanceof RegisterRequest) {
            RegisterRequest req = (RegisterRequest) request;

            int Rid = req.getRQNumb();
            Client.requestMap.put(Rid, request);
            log = "REGISTER REQUEST RID: " + Rid + "\n";
            log(log);
        }
    }

    /**
     * Save response received by the client
     * Removes the Object from the HashMap
     * @param response
     */
    public static void handleReceivedResponse(Object response) {

        // Handle Register confirmation Received
        if (response instanceof ClientRegisterConfirmed) {
            ClientRegisterConfirmed res = (ClientRegisterConfirmed) response;

            // Get the RequestID
            int RequestID = res.getRQNumb();
            // if the RequestID exists in the map remove it
            if (Client.requestMap.containsKey(RequestID)) {
                RegisterRequest req = (RegisterRequest) Client.requestMap.get(RequestID);
                Client.ClientName = req.getClientName();
                Client.requestMap.remove(RequestID);
            }
        }
        else if (response instanceof ClientRegisterDenied) {
            ClientRegisterDenied res = (ClientRegisterDenied) response;

            // Get the RequestID
            int RequestID = res.getRQNumb();
            // if the RequestID exists in the map remove it
            if (Client.requestMap.containsKey(RequestID)) {
                log = "Response of: \n" + Client.requestMap.get(RequestID) + "\n";
                log(log);
                Client.requestMap.remove(RequestID);
            }
            // should probably try to register again
        }
        //add other possible responses
        else {
            log = "THIS RESPONSE CANNOT BE HANDLED";
            log(log);
        }
    }

    /**
     * method to log any message
     * log to command lines and a file
     * @param logText
     */
    public static void log(String logText)
    {
        try {
            Writer.log(logText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}