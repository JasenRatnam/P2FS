import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * class that handles requests from a client
 */
public class ClientHandler implements Runnable {

    // for receiving packets
    private DatagramPacket request;
    // for UDP connection
    private DatagramSocket ds;
    // buffer to store data received from a client
    byte[] dataBuffer;
    // thread to run request from client
    Thread threadClient;
    private static String log;

    /**
     * create a new thread with request from client
     * @param received data from client
     * @param cSocket where data is received from
     */
    public ClientHandler(DatagramPacket received, DatagramSocket cSocket) {
        this.request = received;
        this.ds = cSocket;

        threadClient = new Thread(this);
        threadClient.start();
    }

    /**
     * run client thread with request received
     */
    @Override
    public void run() {

        try {
            //request from client
            this.dataBuffer = request.getData();

            // transform serialized object back into the object
            ByteArrayInputStream byteStream = new ByteArrayInputStream(dataBuffer);
            ObjectInputStream input = new ObjectInputStream(byteStream);
            Object object = input.readObject();

            //print the object received
            System.out.println(object.toString());

            // output to file
            Writer.appendToFile(object);

            //handle request from client
            requestHandler(object);

            //stop reading file
            input.close();
        } catch (IOException e) {
            log = "Exception:  " + e;
            log(log);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to handle the request received from the client
     * @param requestInput
     */
    public synchronized void requestHandler(Object requestInput) {
        //add request to map of requests
        RequestList.received(requestInput, request);

        //if request is a register
        if (requestInput instanceof RegisterRequest) {
            register((RegisterRequest) requestInput);
        }
        else {
            log = "Cannot handle this request.";
            log(log);
        }
    }

    /**
     * Can accept or refuse the registration.
     * Registration can be denied if the provided Name is already in use
     * Registration: send Registered packet
     * Not registered: send Register-Denied
     * Need client unique name, IP Address, UDP socket#, TCP socket#
     * And request number
     */
    public void register(RegisterRequest request) {

        //ask and save name of client
        String username = request.getClientName();
        log = "Register request received";
        log(log);

        // remove request from list of request
        RequestList.remove(request.getRQNumb(), this.request);
    }

    public void log(String logText)
    {
        try {
            Writer.log(logText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}