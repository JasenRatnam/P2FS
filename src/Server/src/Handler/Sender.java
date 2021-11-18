package Handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.lang.System.exit;

/**
 * used to send requests to the server
 */
public class Sender {

    //send a serialised object to the server
    public static void sendTo(Object object, DatagramPacket packet, DatagramSocket clientSocket) {
        String log;
        try {
            // Obtain Client's IP address and the port
            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();

            //make object into bit stream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(object);

            //log into file
            Writer.sendResponse(object, clientAddress.toString(), clientPort);

            //send request to server
            byte[] data = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, clientAddress, clientPort);
            clientSocket.send(sendPacket);

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
}