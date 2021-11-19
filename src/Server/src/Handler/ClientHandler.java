package Handler;

import Requests.DeRegisterRequest;
import Requests.RegisterRequest;
import Requests.Request;
import Responses.RegisterConfirmed;
import Responses.RegisterDenied;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static java.lang.System.exit;

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
            Writer.receiveClient(object);

            //handle request from client
            requestHandler(object);

            //stop reading request
            input.close();
        } catch (IOException e) {
            //e.printStackTrace();
            log = "Receiver IOException " + e.getMessage();
            Writer.log(log);
            exit(1);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            log = "Cannot handle message from server: " + request.toString() + "\n";
            Writer.log(log);
        }
    }

    /**
     * Method to handle the request received from the client
     */
    public synchronized void requestHandler(Object requestInput){
        //add request to map of requests
        if (requestInput instanceof Request request1) {

            //get client IP and port number
            InetAddress clientIp = request.getAddress();
            String ip = clientIp.toString();
            int port = request.getPort();

            //store request with request number
            int RQNumb = request1.getRQNumb();
            String hashId = RQNumb + "-" + ip + ":" + port;
            Server.requestMap.put(hashId, requestInput);

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
                Writer.log(log);
            }
        }
    }

    /**
     * Registration denied if the provided Name or IP is already in use
     * Need client unique name, IP Address, UDP socket#, TCP socket#
     * And request number
     */
    public void register(RegisterRequest request) {
        //save name of client to register
        String username = request.getClientName();
        //save ip of client to register
        String IP = request.getAddress();

        log = "Register request received\n";
        Writer.log(log);
        boolean error = false;
        String errorCode = null;

        //check if client is not already registered
        for ( ClientObject client: Server.clients) {
            if(client.getName().equals(username)){
                //registration denied
                log = "Registration Denied: name -" + username + " - already registered\n";
                error = true;
                errorCode = "Username already exists";
                Writer.log(log);
                break;
            }
            if(client.getIP().equals(IP)){
                //registration denied
                log = "Registration Denied: IP -" + IP + " - already registered\n";
                error = true;
                errorCode = "IP address already exists";
                Writer.log(log);
                break;
            }
        }
        // if not already registered
        if (!error) {
            //register
            Server.clients.add(request.getClientObject());
            Writer.makeServerBackup();
            //Send confirmation too client
            RegisterConfirmed confirmation = new RegisterConfirmed(request.getRQNumb());
            Sender.sendTo(confirmation, this.request, ds);
            log = "Client: " + username + " at " + IP + " has been registered";
        } else {
            //registration denied
            RegisterDenied denied = new RegisterDenied(errorCode, request.getRQNumb());
            Sender.sendTo(denied, this.request, ds);
            log = "Client: " + username + " at " + IP + " has not been registered because: " + errorCode;
        }
        log += "\nServer has " + Server.clients.size() + " client(s)\n";

        Writer.log(log);

        // remove request from list of request
        remove(request.getRQNumb(), this.request);
    }

    /**
     * DeRegister client with given name
     * if no client exists with given name DeRegister
     */
    public void deregister(DeRegisterRequest request) {
        //save name of client
        String username = request.getClientName();

        log = "Deregister request received\n";
        Writer.log(log);

        boolean deregister = false;
        int i = 0;

        for ( ClientObject client: Server.clients) {
            if(client.getName().equals(username)){
                //deregister client
                deregister = true;
                Server.clients.remove(i);
                Writer.makeServerBackup();
                log = client + " has been DeRegistered.\n";
                log += "\nServer has " + Server.clients.size() + " client(s)\n";
                break;
            }
            i++;
        }
        if(!deregister)
        {
            //can't deregister
            log = username + " cannot be DeRegistered.";
            log += "\nCannot find " + username  + "\n";
            log += "\nServer has " + Server.clients.size() + " client(s)\n";
        }
        Writer.log(log);

        // remove request from list of request
        remove(request.getRQNumb(), this.request);
    }

    /**
     * Remove request from the list of requests
     */
    public static void remove(int rid, DatagramPacket packet) {
        //client ip and port
        String ip = packet.getAddress().toString();
        int port = packet.getPort();
        String hashId = rid + "-" + ip + ":" + port;

        //remove request
        Server.requestMap.remove(hashId);
    }
}