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
            // output to file
            Writer.appendToFile(object);

            //handle request from client
            requestHandler(object);

            //stop reading request
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
    public synchronized void requestHandler(Object requestInput) throws IOException {
        //add request to map of requests
        RequestList.received(requestInput, request);

        //if request is a register
        if (requestInput instanceof RegisterRequest) {
            register((RegisterRequest) requestInput);
        }
        //if request is a deregister
        else if (requestInput instanceof DeRegisterRequest) {
            deregister((DeRegisterRequest) requestInput);
        }
        //need to add other requests
        else {
            log = "Cannot handle this request.";
            log(log);
        }
    }

    /**
     * Registration denied if the provided Name or IP is already in use
     * Need client unique name, IP Address, UDP socket#, TCP socket#
     * And request number
     */
    public void register(RegisterRequest request) {
        //save name of client
        String username = request.getClientName();
        //save ip of client
        String IP = request.getAddress();

        log = "Register request received\n";
        log(log);
        boolean error = false;
        String errorCode = null;

        //check if client is not already registered
        for ( ClientObject client: Server.clients) {
            if(client.getName().equals(username)){
                //registration denied
                log = "Registration Denied: name -" + username + " - already registered\n";
                error = true;
                errorCode = "Username already exists";
                log(log);
                break;
            }
            if(client.getIP().equals(IP)){
                //registration denied
                log = "Registration Denied: IP -" + IP + " - already registered\n";
                error = true;
                errorCode = "IP address already exists";
                log(log);
                break;
            }
        }

        try {
            if (!error) {
                // if not already registered
                //registered
                Server.clients.add(request.getClientObject());
                //need to send confirmation too client

                ClientRegisterConfirmed confirmation = new ClientRegisterConfirmed(request.getRQNumb());
                Sender.sendTo(confirmation, this.request, ds);
                log = "Client: " + username + " at " + IP + " has been registered";
            } else {
                //registration denied
                ClientRegisterDenied denied = new ClientRegisterDenied(errorCode, request.getRQNumb());
                Sender.sendTo(denied, this.request, ds);
                log = "Client: " + username + " at " + IP + " has not been registered because: " + errorCode;
            }
            log += "\nServer has " + Server.clients.size() + " client(s)\n";
        } catch (IOException e) {
            e.printStackTrace();
        }

        log(log);

        // remove request from list of request
        RequestList.remove(request.getRQNumb(), this.request);
    }

    /**
     * Deregsiter client with given name
     * if no client exists with given name deregister
     *
     * @param request
     */
    public void deregister(DeRegisterRequest request) {
        //save name of client
        String username = request.getClientName();

        log = "Dergister request received\n";
        log(log);

        boolean deregister = false;
        int i = 0;

        for ( ClientObject client: Server.clients) {
            if(client.getName().equals(username)){
                //deregister client
                deregister = true;
                Server.clients.remove(i);
                log = client.toString() + " has been deregistered.\n";
                log += "\nServer has " + Server.clients.size() + " client(s)\n";
                break;
            }
            i++;
        }
        if(!deregister)
        {
            //can't deregister
            log = username + " cannot be deregistered.";
            log += "\nCannot find " + username  + "\n";
            log += "\nServer has " + Server.clients.size() + " client(s)\n";
        }
        log(log);

        // remove request from list of request
        RequestList.remove(request.getRQNumb(), this.request);
    }

    /**
     * method to log any message
     * log to command lines and a file
     * @param logText
     */
    public void log(String logText)
    {
        try {
            Writer.log(logText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}