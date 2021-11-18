package Handler;


import Requests.DownloadRequest;
import Requests.Request;
import Responses.DownloadError;
import Responses.RegisterConfirmed;
import Responses.RegisterDenied;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.exit;

public class TCPServerHandler implements Runnable{

    private static ObjectInputStream InputStream = null;
    private static ObjectOutputStream OutputStream = null;
    private ServerSocket server;
    private Socket client;
    private static String log;
    public static ConcurrentHashMap<String,Object> requestMap = new ConcurrentHashMap<>();


    /**
     * constructor to handle responses from the server
     * handle socket
     */
    public TCPServerHandler(){
        try{
            this.server = new ServerSocket(Client.clientTCPPort);
        } catch (IOException e) {
            //e.printStackTrace();
            log = "Receiver IOException " + e.getMessage();
            Writer.log(log);
            exit(1);
        }
    }

    @Override
    public void run() {
        log = "\nRunning TCP Server... \n";
        Writer.log(log);

        //waiting for a message via TCP from another client
        try {
            while (true) {
                log = "listening to port: " + Client.clientTCPPort + "\n";

                //wait for a TCP connection request

                //accept a client connection in socket
                client = this.server.accept();
                log = client +"is connected.\n";

                //initialise input and output streamers
                InputStream = new ObjectInputStream(client.getInputStream());
                OutputStream = new ObjectOutputStream(client.getOutputStream());

                try{
                    //get object received from a client
                    Object object =  InputStream.readObject();

                    // add object to log file
                    Writer.receiveObject(object);

                    log =  "From: " + client;
                    Writer.log(log);

                    // handle the TCP request
                    requestHandler(object);
                } catch (ClassNotFoundException e) {
                    //e.printStackTrace();
                    log = "Cannot handle message from client: " + client.toString() + "\n";
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
     * Method to handle the request received from the client
     */
    public synchronized void requestHandler(Object input){
        //if the received TCP request is a type of known request
        if (input instanceof Request request1) {

            //get client IP and port number
            InetAddress clientIp = client.getInetAddress();
            String ip = clientIp.toString();
            int port = client.getPort();

            //store request number
            int RQNumb = request1.getRQNumb();
            String hashId = RQNumb + "-" + ip + ":" + port;
            requestMap.put(hashId, input);

            //if input is a download
            if (input instanceof DownloadRequest) {
                //hanndle the download request
                download((DownloadRequest) input);
                //download file
            }
            else {
                //ignore any other request that comes through the TCP port
                log = "Cannot handle this request.";
                Writer.log(log);
            }
        }
    }

    /**
     * A client has sent a download request
     */
    public void download(DownloadRequest request){
        boolean error = true;
        String errorCode = null;
        //find file
        //if cant find file dwonload error
        //if can find file start sending file

        // if not already registered
        if (!error) {
            //start file transfer

            //do transfer



        } else {
            //download denied
            errorCode = "error";
            DownloadError downloadErrorMessage = new DownloadError(request.getRQNumb(), errorCode);
            try{
                OutputStream.writeObject(downloadErrorMessage);
                Writer.sendRequest(downloadErrorMessage, client.getInetAddress().toString(),client.getPort());
                log = "Download cannot happen because: " + errorCode + "\n";
                Writer.log(log);
            } catch (IOException e) {
                //e.printStackTrace();
                log = "Can't handle the request";
                Writer.log(log);
            }
        }
        // remove request from list of request
        remove(request.getRQNumb(), client);
    }

    /**
     * Remove request from the list of requests
     */
    public static void remove(int rid, Socket client){
        //client ip and port
        String ip = client.getInetAddress().toString();
        int port = client.getPort();
        String hashId = rid + "-" + ip + ":" + port;

        //remove request
        requestMap.remove(hashId);
    }
}