import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * used to send requests to the server
 */
public class Sender {

    //send a serialised object to the server
    public static void sendTo(Object object, DatagramSocket datagramSocket, String address, int port)  throws IOException {
        try {
            //make object into bit stream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(object);

            //log into file
            Writer.sendRequest(object, address, port);

            //add request to list of requests sent to server
            RequestList.handleSentRequest(object);

            //send request to server
            byte[] data = outputStream.toByteArray();
            InetAddress addr =InetAddress.getByName(address);
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, addr, port);
            datagramSocket.send(sendPacket);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}