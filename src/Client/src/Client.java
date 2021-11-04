//socket with UDP
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static InetAddress serverIp;
    private static InetAddress clientIp;
    private static int serverPort;
    private static DatagramSocket ds;
    private static DatagramPacket request,response;
    private static byte[] receive = null;
    private static byte[] requestByte = null;
    private static Scanner sc;

    public Client() throws  SocketException{
        ds = new DatagramSocket();
    }

    /**
     * Main method
     * @param args
     * @throws IOException
     */
    public static void main(String args[]) {
        initialiseClient();
    }

    /**
     * intialise client and connect to server
     */
    private static void initialiseClient(){
        //scanner to read client input
        sc = new Scanner(System.in);

        try {
            //ask and get IP of server
            System.out.println("Enter IP address of the Server: ");
            String ip = sc.nextLine();
            serverIp = InetAddress.getByName(ip);

            //ask and get port of server
            System.out.println("Enter port number of the Server: ");
            String port = sc.nextLine();
            serverPort = Integer.parseInt(port);

            System.out.println("Connecting to server \n");
            //open port to receive data
            Client client = new Client();
            client.UDPService();

            clientIp = InetAddress.getLocalHost();
            System.out.println("Client information: \nIP: " +  clientIp + "\n");

        } catch (SocketException ex) {
            System.out.println("Socket error: " + ex.getMessage());
        } catch(UnknownHostException uhEx) {
            System.out.println("HOST ID not found.. ");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("\nClosing client.... ");
            ds.close();
        }
    }

    private void UDPService() throws IOException {
        //array to store message received
        receive = new byte[65535];
        requestByte = new byte[65535];

        while (true) {
            //ask and get message to be sent to server
            System.out.println("Enter message for Server: ");
            String inp = sc.nextLine();

            //convert gotten message to a buffer of bytes and send it to the server
            requestByte = inp.getBytes();
            request = new DatagramPacket(requestByte, requestByte.length, serverIp, serverPort);
            ds.send(request);

            System.out.println("Message sent to Server: \nIP: " + serverIp.toString() + " \nPort: " +
                    serverPort + "\nMessage: -" + inp + "\n");

            //get message from server
            response = new DatagramPacket(receive, receive.length);
            ds.receive(response);

            System.out.println("Message from Server: \nIP: " + serverIp.toString() + " \nPort: " +
                    serverPort + "\nMessage: -" + data(receive) + "\n");

            //Thread.sleep(10000);

            //if message sent is bye, then close the connection
            if (inp.equals("bye"))
                break;

            // Clear the buffers after every message.
            receive = new byte[65535];
            requestByte = new byte[65535];
        }
    }
    /**
     * A method to convert the byte array data into a string representation.
     * @param a byte array to transform to a string
     * @return sstringbuilder from byte data
     */
    public static StringBuilder data(byte[] a) {
        if (a == null)
            return null;

        StringBuilder ret = new StringBuilder();

        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
}