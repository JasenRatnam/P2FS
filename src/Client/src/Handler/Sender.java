package Handler;

import Requests.Request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

import static java.lang.System.exit;

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