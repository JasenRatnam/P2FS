package Handler;


import Requests.DownloadRequest;
import Requests.RegisterRequest;
import Requests.Request;
import Responses.DownloadError;
import Responses.RegisterConfirmed;
import Responses.RegisterDenied;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerHandler implements Runnable{

    private static ObjectInputStream InputStream = null;
    private static ObjectOutputStream OutputStream = null;
    private ServerSocket server;
    private static String log;

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

        try {
            while (true) {
                System.out.println("listening to port: " + Client.clientTCPPort);
                Socket client = this.server.accept();
                System.out.println(client +" connected.");

                InputStream = new ObjectInputStream(client.getInputStream());
                OutputStream = new ObjectOutputStream(client.getOutputStream());

                try{
                    Object object =  InputStream.readObject();
                    System.out.println("Received [" + object.toString() + "] messages from: " + client);


                    // add object to log file
                    Writer.receiveServer(object);

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
        if (input instanceof Request) {
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
        //find file
        //if cant find file dwonload error
        //if can find file start sending file
        System.out.println("Sending messages to the client");
        String reason = "error";
        DownloadError downloadErrorMessage = new DownloadError(request.getRQNumb(), reason);
        OutputStream.writeObject(downloadErrorMessage);
    }
}
