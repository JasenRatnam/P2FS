import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerHandler implements Runnable {

    private static DatagramSocket ds;
    private static DatagramPacket response;
    private static byte[] receive = null;
    private static String log;


    /**
     * constructor to handle responses from the server
     * @param clientSocket
     */
    public ServerHandler(DatagramSocket clientSocket) {
        this.ds = clientSocket;
    }

    /**
     * run responses from server in a thread
     */
    @Override
    public void run() {
        try {
            while (true) {

                //get and save the response from the server
                receive = new byte[65535];
                response = new DatagramPacket(receive, receive.length);
                ds.receive(response);

                // get request object from server
                byte[] dataBuffer = response.getData();
                ByteArrayInputStream byteStream = new ByteArrayInputStream(dataBuffer);
                ObjectInputStream is = new ObjectInputStream(byteStream);

                log = "---\nRECEIVED: ";
                log(log);
                try{
                    Object o = (Object) is.readObject();

                    // add object to log file
                    Writer.appendToFile(o);
                    // remove request associated to response from list of requests
                    RequestList.handleReceivedResponse(o);
                    // handle the response
                    requestHandler(o, response);
                } catch (ClassNotFoundException e) {
                //e.printStackTrace();
                log = "Cannot handle message from server: " + response.toString() + "\n---\n";
                log(log);
                }
            }
        } catch (IOException e) {
            log = "Receiver IOException " + e.getMessage();
            log(log);
        }
    }

    /**
     * method to handle the response from the server
     * @param request
     * @param packet
     */
    public void requestHandler(Object request, DatagramPacket packet) {
        // Handle Successful Register Request - Don't think we need it
        if (request instanceof ClientRegisterConfirmed) {
            Client.isRegistered = true;
            log = "You are now registered.\n---\n";
            log(log);
        }
        else if(request instanceof ClientRegisterDenied){
            Client.isRegistered = false;
            log = "Registration Denied: You are not registered.\n---\n";
            log(log);
        }
        //add other if to handle other possible response by server
        else {
            //cant handle response
            log = "Cannot handle this response: " + request.toString();
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
