package Handler;


import Requests.DownloadRequest;
import Requests.Request;
import Responses.DownloadError;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.exit;

/**
 * Thread that runs the TCP server of the client
 * keeps listening to any message
 * Handle any message received
 */
public class TCPServerHandler implements Runnable{

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
            //open TCP socket
            this.server = new ServerSocket(Client.clientTCPPort);
        } catch (IOException e) {
            //e.printStackTrace();
            log = "TCP receiver IOException " + e.getMessage();
            Writer.log(log);
            exit(1);
        }
    }

    //thread
    @Override
    public void run() {
        log = "\nRunning TCP Server... \n";
        Writer.log(log);

        try {
            //waiting for a message via TCP from another client
            while (true) {
                log = "listening to port: " + Client.clientTCPPort + "\n";

                //wait for a TCP connection request

                //accept a client connection in socket
                //connect to the client
                client = this.server.accept();
                log = client +"is connected.\n";

                //initialise input and output streamers
                ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
                OutputStream = new ObjectOutputStream(client.getOutputStream());

                try{
                    //get object received from a client
                    Object object =  inputStream.readObject();

                    // add object to log file
                    Writer.receiveObject(object);

                    log =  "From: " + client;
                    Writer.log(log);

                    // handle the TCP request
                    requestHandler(object);
                } catch (ClassNotFoundException e) {
                    //e.printStackTrace();
                    log = "Cannot handle message from TCP client: " + client.toString() + "\n";
                    Writer.log(log);
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
            log = "TCP Receiver IOException " + e.getMessage();
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
                //handle the download request
                download((DownloadRequest) input);
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
     * if file exist upload it to the client
     * if it does not exist, send download-error
     */
    public void download(DownloadRequest request){
        boolean error = false;
        String errorCode = null;

        //get file name
        String fileName =  request.getFileName();

        //add extension if not alredy there
        String filePath;
        if(fileName.endsWith(".txt"))
        {
            filePath = fileName;
        }
        else
        {
            filePath = fileName + ".txt";
        }

        //check if file exists
        //find file
        //if cant find file dwonload error
        //if can find file start sending file
        File f = new File(filePath);
        if(!f.isFile()) {
            // do something
            error = true;
            errorCode = "File does not exist";
        }

        BufferedReader reader;
        //if file exists
        if (!error) {
            //start file transfer
            //do transfer
            try {
                //start reading wanted text file
                FileInputStream fileStream = new FileInputStream(f);
                InputStreamReader input = new InputStreamReader(fileStream);
                reader = new BufferedReader(input);

                int chunkNumb = 1;
                int charCount = 0;
                String line;
                StringBuilder data = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    charCount += line.length();
                    data.append(line);
                    if(charCount >= 200){
                        //segment has 200 character
                        //create object with segment
                        Responses.File fileResponse = new Responses.File(request.getRQNumb(), filePath,chunkNumb, data.toString());

                        //send object of file and log
                        try{
                            OutputStream.writeObject(fileResponse);
                            Writer.sendRequest(fileResponse, client.getInetAddress().toString(),client.getPort());
                            log = "Sending file: " + fileName + ", Chunk number: " + chunkNumb + "\n";
                            Writer.log(log);
                        } catch (IOException e) {
                            e.printStackTrace();
                            log = "Can't handle the request1";
                            Writer.log(log);
                        }

                        //reset counter
                        charCount = 0;
                        data = new StringBuilder();
                        chunkNumb++;
                    }
                }
                //create and send the last segment
                Responses.FileEnd fileEndResponse = new Responses.FileEnd(request.getRQNumb(), filePath,chunkNumb, data.toString());
                try{
                    OutputStream.writeObject(fileEndResponse);
                    Writer.sendRequest(fileEndResponse, client.getInetAddress().toString(),client.getPort());
                    log = "Sending file : " + fileName + " ending, Final chunk number: " + chunkNumb + "\n";
                    Writer.log(log);
                } catch (IOException e) {
                    //e.printStackTrace();
                    log = "Can't handle the request";
                    Writer.log(log);
                }
            } catch (IOException e) {
                //e.printStackTrace();
                log = "Can't handle the request";
                Writer.log(log);
            }
        } else {
            //download denied
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
