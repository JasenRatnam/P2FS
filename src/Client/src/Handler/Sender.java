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
            log += "\nClosing client....\n ";
            Writer.log(log);
            exit(1);
        } catch (IOException e) {
            //e.printStackTrace();
            log = "IOException.... ";
            log += "\nClosing client....\n ";
            Writer.log(log);
            exit(1);
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

            //add request to list of requests sent to server
            saveRequest(object);

            //wait for a response
            //get response
            Object response =  In.readObject();
            System.out.println("Received [" + response.toString() + "] messages from: " + socket);

            int RequestID;
            //if response is a known request type
            if (response instanceof Request res) {
                // Get the RequestID
                RequestID = res.getRQNumb();

                log = "Response of: \n" + Client.requestMap.get(RequestID) + "\n";
                Writer.log(log);

                // Handle Successful Register
                if (response instanceof DownloadError) {
                    DownloadError downloadErrorMessage = (DownloadError) response;
                    log = "Received [" + downloadErrorMessage.toString() + "] messages from: " + socket + "\n";

                    log += "Download has failed.\n";
                    Writer.log(log);
                } else if (response instanceof File) {
                    //start recreating file
                }
                else if (response instanceof FileEnd) {
                    //end of file creation
                }
                else {
                    //cant handle response
                    log = "Cannot handle this response: " + response;
                    Writer.log(log);
                }
                //remove request ID from list
                Client.requestMap.remove(RequestID);
            }

            socket.close();
            out.close();
            In.close();
        } catch (UnknownHostException e) {
            //e.printStackTrace();
            log = "HOST ID not found.... ";
            log += "\nClosing client....\n ";
            Writer.log(log);
            exit(1);
        } catch (IOException e) {
            //e.printStackTrace();
            log = "IOException.... ";
            log += "\nClosing client....\n ";
            Writer.log(log);
            exit(1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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