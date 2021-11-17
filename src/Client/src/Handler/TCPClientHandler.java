package Handler;


import Requests.Download;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPClientHandler implements Runnable{

    private static ObjectInputStream InputStream = null;
    private ServerSocket server;
    private static String log;

    /**
     * constructor to handle responses from the server
     * handle socket
     */
    public TCPClientHandler() throws IOException {
        this.server = new ServerSocket(Client.clientTCPPort);
    }

    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }

    public int getPort() {
        return this.server.getLocalPort();
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


                try{
                    Download downloadRequest = (Download) InputStream.readObject();
                    System.out.println("Received [" + downloadRequest.toString() + "] messages from: " + client);


                    // add object to log file
                    Writer.receiveServer(downloadRequest);

                    // handle the message
                    //responseHandler(o);
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
}
