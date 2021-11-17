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
    public TCPServerHandler() throws IOException {
        this.server = new ServerSocket(Client.clientTCPPort);
    }

    @Override
    public void run() {
        System.out.println("\r\nRunning TCP Server... ");

        //waiting for a message via TCP from another client
        try {
            while (true) {
                System.out.println("listening to port: " + Client.clientTCPPort);
                //wait for a TCP message
                client = this.server.accept();
                System.out.println(client +" connected.");

                //initialise input and output streamers
                InputStream = new ObjectInputStream(client.getInputStream());
                OutputStream = new ObjectOutputStream(client.getOutputStream());

                try{
                    //get object received from a client
                    Object object =  InputStream.readObject();
                    System.out.println("Received [" + object.toString() + "] messages from: " + client);


                    // add object to log file
                    Writer.receiveObject(object);

                    // handle the message
                    requestHandler(object);
                } catch (ClassNotFoundException e) {
                    //e.printStackTrace();
                    log = "Cannot handle message from client";
                    Writer.log(log);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to handle the request received from the client
     */
    public synchronized void requestHandler(Object input) throws IOException {
        //add request to map of requests
        if (input instanceof Request request1) {

            //get client IP and port number
            InetAddress clientIp = client.getInetAddress();
            String ip = clientIp.toString();
            int port = client.getPort();

            //store request with request number
            int RQNumb = request1.getRQNumb();
            String hashId = RQNumb + "-" + ip + ":" + port;
            requestMap.put(hashId, input);


            //if input is a download
            if (input instanceof DownloadRequest) {
                download((DownloadRequest) input);
                //download file
            }
            else {
                log = "Cannot handle this request.";
                Writer.log(log);
            }
        }
    }

    /**
     * A client has sent a download request
     */
    public void download(DownloadRequest request) throws IOException {
        boolean error = true;
        String errorCode = null;
        //find file
        //if cant find file dwonload error
        //if can find file start sending file

        // if not already registered
        if (!error) {
            //start file transfer
        } else {
            //download denied
            errorCode = "error";
            DownloadError downloadErrorMessage = new DownloadError(request.getRQNumb(), errorCode);
            OutputStream.writeObject(downloadErrorMessage);
            log = "Download cannot happen because: " + errorCode;
            Writer.log(log);
        }
        // remove request from list of request
        remove(request.getRQNumb(), client);
    }

    /**
     * Remove request from the list of requests
     */
    public static void remove(int rid, Socket client) {
        //client ip and port
        String ip = client.getInetAddress().toString();
        int port = client.getPort();
        String hashId = rid + "-" + ip + ":" + port;

        //remove request
        requestMap.remove(hashId);
    }
}
