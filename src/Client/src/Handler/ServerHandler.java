package Handler;

import Requests.PublishRequest;
import Requests.RegisterRequest;
import Requests.RemoveRequest;
import Requests.Request;
import Responses.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static java.lang.System.exit;

public class ServerHandler implements Runnable {

    private static DatagramSocket ds;
    private static DatagramPacket response;
    private static byte[] receive = null;
    private static String log;

    /**
     * constructor to handle responses from the server
     * handle socket
     */
    public ServerHandler(DatagramSocket clientSocket) {
        ds = clientSocket;
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

                try{
                    Object o = is.readObject();

                    // add object to log file
                    Writer.receiveServer(o);

                    // handle the response
                    responseHandler(o);
                } catch (ClassNotFoundException e) {
                    //e.printStackTrace();
                    log = "Cannot handle message from server: " + response.toString() + "\n";
                    Writer.log(log);
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
            log = "Receiver IOException " + e.getMessage();
            Writer.log(log);
            exit(1);
        }
    }

    /**
     * method to handle the response from the server
     */
    public void responseHandler(Object response) {

        int RequestID;
        //if response is a known request type
        if (response instanceof Request res) {
            // Get the RequestID
            RequestID = res.getRQNumb();

            log = "Response of: \n" + Client.requestMap.get(RequestID) + "\n";
            Writer.log(log);

            // Handle Successful Register
            if (response instanceof RegisterConfirmed) {
                //client is set to registered
                Client.isRegistered = true;

                //save registered name as name of client
                if (Client.requestMap.containsKey(RequestID)) {
                    RegisterRequest req = (RegisterRequest) Client.requestMap.get(RequestID);
                    //save registered name of client
                    Client.ClientName = req.getClientName();
                }

                log = "You are now registered.\n";
                Writer.log(log);
            } else if (response instanceof RegisterDenied) {
                //if register is denied
                Client.isRegistered = false;

                log = "Registration Denied: You are not registered.\n";
                Writer.log(log);
            } else if (response instanceof PublishConfirmed)
            {
                //publish the clients files
                if (Client.requestMap.containsKey(RequestID)) {
                   PublishRequest req = (PublishRequest) Client.requestMap.get(RequestID);
                    //save registered name of client
                  //  Client. = req.getClientName();
                }
            }else if (response instanceof PublishDenied)
            {

            }else if (response instanceof RemoveConfirmed)
            {

            }else if (response instanceof RemoveDenied)
            {

            }




            //add other if to handle other possible response by server

            else {
                //cant handle response
                log = "Cannot handle this response: " + response;
                Writer.log(log);
            }
            //remove request ID from list
            Client.requestMap.remove(RequestID);
        }

    }


}
