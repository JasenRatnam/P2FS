import java.io.IOException;

public class RequestList {

    private static String log;

    /**
     * Save request sent by the client to the HashMap
     *
     * @param
     */
    public static void handleSentRequest(Object request) {

        // Handle Register Request
        if (request instanceof RegisterRequest) {
            RegisterRequest req = (RegisterRequest) request;

            int Rid = req.getRQNumb();
            Client.requestMap.put(Rid, request);
            log = "REGISTER REQUEST RID: " + Rid;
            log(log);
        }
    }

    /**
     * Save response received by the client
     * Removes the Object from the HashMap
     *
     * @param response
     */
    public static void handleReceivedResponse(Object response) {

        log = "RECEIVED RESPONSE: ";
        log += response.toString();
        log(log);

        // Handle Register confirmation Received
        if (response instanceof ClientRegisterConfirmed) {
            ClientRegisterConfirmed res = (ClientRegisterConfirmed) response;

            // Get the RequestID
            int RequestID = res.getRQNumb();
            // if the RequestID exists in the map remove it
            if (Client.requestMap.containsKey(RequestID)) {
                log = "Response of: " + Client.requestMap.get(RequestID);
                log(log);
                Client.requestMap.remove(RequestID);
            }
        }
        //add other possible responses
        else {
            log = "THIS RESPONSE CANNOT BE HANDLED";
            log(log);
        }
    }

    public static void log(String logText)
    {
        try {
            Writer.log(logText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}