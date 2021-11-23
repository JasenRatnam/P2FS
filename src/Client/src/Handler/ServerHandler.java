package Handler;

import Requests.*;
import Responses.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static java.lang.System.exit;

public class ServerHandler implements Runnable {

    private static DatagramSocket ds;
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
                byte[] receive = new byte[65535];
                DatagramPacket response = new DatagramPacket(receive, receive.length);
                ds.receive(response);

                // get request object from server
                byte[] dataBuffer = response.getData();
                ByteArrayInputStream byteStream = new ByteArrayInputStream(dataBuffer);
                ObjectInputStream is = new ObjectInputStream(byteStream);

                try{
                    Object o = is.readObject();

                    // add object to log file
                    Writer.receiveObject(o);

                    // handle the response
                    responseHandler(o);
                } catch (ClassNotFoundException e) {
                    //e.printStackTrace();
                    log = "Cannot handle message from server: " + response + "\n";
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
        if (response instanceof Request) {
            Request res = (Request) response;

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
                RegisterDenied regDen = (RegisterDenied) response;

                //if register is denied
                Client.isRegistered = false;

                log = "Registration Denied: You are not registered.\n";
                log += regDen.getReason() + "\n";
                Writer.log(log);
            } else if (response instanceof PublishConfirmed) {
                log = "Publish confirmed: Files have been published to server.\n";
                Writer.log(log);

                //get list of files published
                //add files to yourself
                if (Client.requestMap.containsKey(RequestID)) {
                    Client.listOfFile.clear();
                    PublishRequest req = (PublishRequest) Client.requestMap.get(RequestID);
                    Client.listOfFile.addAll(req.getListOfFiles());
                }
                log = "Client has total published files: " + Client.listOfFile.toString() + "\n";
                Writer.log(log);
            }else if (response instanceof PublishDenied)
            {
                PublishDenied pubDen = (PublishDenied) response;
                log = "Publish Denied: files have not been published to server.\n";
                log += pubDen.getReason() + "\n";
                Writer.log(log);
            }else if (response instanceof RemoveConfirmed) {

                //get list of files removed
                //remove files to yourself
                if (Client.requestMap.containsKey(RequestID)) {
                    RemoveRequest req = (RemoveRequest) Client.requestMap.get(RequestID);
                    Client.listOfFile.remove(req.getListOfFiles());
                }
                log = "Removed confirmed: Files have been Removed from the server.\n";
                log += "Client has total published files: " + Client.listOfFile.toString() + "\n";
                Writer.log(log);
            }else if (response instanceof RemoveDenied) {
                RemoveDenied removeDenied = (RemoveDenied) response;

                log = "Remove Denied: files have not been Removed from the server.\n";
                log += removeDenied.getReason() + "\n";
                Writer.log(log);
            }else if (response instanceof UpdateConfirmed) {
                UpdateConfirmed upConf = (UpdateConfirmed) response;
                log = "Update confirmed: Client information has been updated.\n";

                //print new client information
                log += "\nClient information updated too: " +
                        "\nIP: " + upConf.getIPaddress().toString() +
                        "\nClient name: " + upConf.getClientName() +
                        "\nUDP Port: " + upConf.getUDPport() +
                        "\nTCP Port: " + upConf.getTCPport() + "\n";
                Writer.log(log);

                try {
                    Client.ClientName = upConf.getClientName();
                    Client.clientIp = InetAddress.getLocalHost();
                    Client.clientUDPPort = upConf.getUDPport();
                    Client.clientTCPPort = upConf.getTCPport();
                    //client is set to registered
                    Client.isRegistered = true;
                } catch (IOException e)
                {
                    //e.printStackTrace();
                    log = "Update IOException " + e.getMessage();
                    log += "Update has failed, try again later.";
                    Writer.log(log);
                }
            }else if (response instanceof UpdateDenied) {
                UpdateDenied updDen = (UpdateDenied) response;

                log = "Update Denied: update could not happen.\n";
                log += updDen.getReason() + "\n";
                Writer.log(log);
            }
            else if (response instanceof Retrieve)
            {
                Retrieve retrieve = (Retrieve) response;

                log = "Retrieve Confirmed: List of available clients shown below:\n";

                for ( ClientObject client: retrieve.getClients()) {
                    log += client.getName() + " " + client.getIP() + " TCP: " + client.getTCPport() + " FILES:" + client.getFiles() + "\n";
                }

                Writer.log(log);
            } else if (response instanceof RetrieveError)
            {
                RetrieveError retrieveError = (RetrieveError) response;

                log = "Retrieve Denied: Cannot retreive client(s) from server.\n";
                log += "Because: " + retrieveError.getReason() + "\n";
                Writer.log(log);
            } else if (response instanceof RetrieveInfoT)
            {
                RetrieveInfoT retreiveInfo = (RetrieveInfoT) response;

                log = "Retrieve InfoT Confirmed: Client " + retreiveInfo.getClientName() + " shown below:\n";

                log += retreiveInfo.getClientName() + " " + retreiveInfo.getAddress() + " TCP: " + retreiveInfo.getTCPport() + " FILES:" + retreiveInfo.getListOfFiles() + "\n";

                Writer.log(log);
            }else if (response instanceof SearchFileResponse)
            {
                SearchFileResponse fileresponse = (SearchFileResponse) response;

                log = "Search file Confirmed: List of client(s) with file shown below:\n";
                for ( ClientObject client: fileresponse.getClients()) {
                    log += client.getName() + " " + client.getIP() + " TCP: " + client.getTCPport()  + "\n";
                }
                Writer.log(log);
            }else if(response instanceof SearchError)
            {
                SearchError error = (SearchError) response;

                log = "SearchFile Denied: Cannot retreive file from server.\n";
                log += "Because: " + error.getReason() + "\n";
                Writer.log(log);
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
