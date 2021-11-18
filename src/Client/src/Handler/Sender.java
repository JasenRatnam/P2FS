package Handler;

import Requests.RegisterRequest;
import Requests.Request;
import Responses.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.List;

import static java.lang.System.exit;
import static java.lang.System.in;

/**
 * used to send requests to the server
 */
public class Sender {

    private static String log;

    //send a serialised object to the server
    public static void sendTo(Object object, DatagramSocket datagramSocket, String address, int port) {
        try {
            //make object into bit stream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(object);

            //log into log file
            Writer.sendRequest(object, address, port);

            //add request to list of requests sent to server
            saveRequest(object);

            //send request to server
            byte[] data = outputStream.toByteArray();
            InetAddress serverIP =InetAddress.getByName(address);
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverIP, port);
            datagramSocket.send(sendPacket);

        } catch (UnknownHostException e) {
            //e.printStackTrace();
            log = "HOST ID not found.... ";
            log += "\nSending file failed... Try again later\n ";
            Writer.log(log);
        } catch (IOException e) {
            //e.printStackTrace();
            log = "IOException.... ";
            log += "\nSending file failed... Try again later\n ";
            Writer.log(log);
        }
    }

    /**
     * send a serialised object to the wanted client
     * Use TCP protocol
     * Wait for a response before closing connection
     */
    public static void sendToTCP(Object object, String clientAddress, int clientPort) {
        //try TCP socket connection to wanted client
        try(Socket socket = new Socket(clientAddress,clientPort)) {
            //object streams
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream In = new ObjectInputStream(socket.getInputStream());

            //send object to the client via TCP socket
            out.writeObject(object);

            //log sending into log file
            Writer.sendRequest(object, clientAddress, clientPort);

            //add request to list of requests sent
            saveRequest(object);

            //wait for a response
            //get response
            Object response =  In.readObject();

            Writer.receiveObject(response);

            log =  "From: " + socket;
            Writer.log(log);

            //keep reading while response is of type File
            while(response instanceof File)
            {
                //continue getting messages until you get File End
                //do not close socket until you get File End

                //handle file response
                handleTCPResponse(response);

                //wait for next resonse
                //get response
                response =  In.readObject();

                Writer.receiveObject(response);

                log =  "From: " + socket;
                Writer.log(log);
            }

            handleTCPResponse(response);

            socket.close();
            out.close();
            In.close();
        } catch (UnknownHostException e) {
            //e.printStackTrace();
            log = "HOST ID not found.... ";
            log += "\nSending file failed... Try again later\n ";
            Writer.log(log);
        } catch (IOException e) {
            //e.printStackTrace();
            log = "IOException.... ";
            log += "\nSending file failed... Try again later\n ";            Writer.log(log);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            log = "Cannot handle message from client\n";
            Writer.log(log);
        }
    }

    public static void handleTCPResponse(Object response)
    {
        int RequestID;
        //if TCP response is a known request type
        if (response instanceof Request res) {
            // Get the RequestID
            RequestID = res.getRQNumb();

            log = "Response of: \n" + Client.requestMap.get(RequestID) + "\n";
            Writer.log(log);

            //handle the TCP response

            // if response is a download error
            if (response instanceof DownloadError) {

                //download has failed
                log = "Download has failed. Please try again later\n";
                Writer.log(log);
                //remove request ID from list
                Client.requestMap.remove(RequestID);
            } else if (response instanceof File chunkOfText) {
                //start recreating file
                Writer.downloadFile(chunkOfText.getText(), chunkOfText.getFileName());
            }
            else if (response instanceof FileEnd chunkOfText) {
                //end of file creation
                Writer.downloadFile(chunkOfText.getText(), chunkOfText.getFileName());
                //remove request ID from list
                Client.requestMap.remove(RequestID);
            }
            else {
                //cant handle response
                log = "Cannot handle this response: " + response;
                Writer.log(log);
            }
        }
    }

    /**
     * Save request sent by the client to the HashMap
     */
    public static void saveRequest(Object request) {
        // Handle Requests.Request
        Request req = (Request) request;
        int Rid = req.getRQNumb();

        Client.requestMap.put(Rid, request);

        log = "REQUEST RID: " + Rid + " saved\n";
        Writer.log(log);
    }
}