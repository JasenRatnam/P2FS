package Handler;

import Requests.Request;
import Responses.DownloadError;

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

    //send a serialised object to the client
    public static void sendToTCP(Object object, String clientAddress, int clientPort) {
        try(Socket socket = new Socket(clientAddress,clientPort)) {
            //make object into bit stream
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream In = new ObjectInputStream(socket.getInputStream());
            out.writeObject(object);

            //log into log file
            Writer.sendRequest(object, clientAddress, clientPort);

            //add request to list of requests sent to server
            saveRequest(object);

            //wait for a response
            DownloadError downloadErrorMessage = (DownloadError) In.readObject();
            System.out.println("Received [" + downloadErrorMessage.toString() + "] messages from: " + socket);

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

        log = "REGISTER REQUEST RID: " + Rid + "\n";
        Writer.log(log);
    }
}