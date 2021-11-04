//socket with UDP
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Server {
    private static DatagramSocket ds;
    private static InetAddress serverIp;
    private static int serverPort ;
    private static DatagramPacket request, response;
    private static byte[] receive = null;
    private static byte[] responseByte = null;
    private static Scanner sc;

    /**
     * associate port to server and open it to clients
     * @param port to be opened
     * @throws SocketException
     */
    public Server(int port) throws  SocketException{
        ds = new DatagramSocket(port);
    }

    /**
     * Main method
     * run initialiseServer method
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        initialiseServer();
    }

    /**
     * intialise IP and port number of the server
     * Run server using UDPService
     */
    private static void initialiseServer(){
        //scanner to read server input
        sc = new Scanner(System.in);

        try {

            //ask and get port of server
            System.out.println("Enter port number of the Server: ");
            String port = sc.nextLine();
            serverPort = Integer.parseInt(port);

            // address of server
            serverIp = InetAddress.getLocalHost();

            System.out.println("opening port \n");
            //open port to receive data
            Server server = new Server(serverPort);

            System.out.println("Server information: " +
                    "\nIP: " +  serverIp.toString() +
                    "\nPort: " + serverPort + "\n");

            //start UDP
            server.UDPService();

        } catch (SocketException ex) {
            System.out.println("Socket error: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
        finally {
            System.out.println("\nClosing server....");
            ds.close();
        }
    }

    /**
     * Collect data from clients and respond
     * UDP service
     * get a request and send a response to client
     * @throws IOException
     */
    private void UDPService() throws IOException {
        //array to store message received
        receive = new byte[65535];
        //array to store message sent
        responseByte = new byte[65535];

        while (true) {
            //receive request gotten from client
            request = new DatagramPacket(receive, receive.length);
            ds.receive(request);

            // Get IP and Port of client
            InetAddress clientIp = request.getAddress();
            int clientPort = request.getPort();

            String dataReceived = data(receive).toString();
            System.out.println("Message from Client: " +
                    "\nIP: " + clientIp.toString() +
                    "\nPort: " + clientPort +
                    "\nMessage: -" + dataReceived + "\n");

            //create response message to client
            String quote = "Message received: '" + dataReceived + "' from IP:" + clientIp.toString() +
                    ", Port: " + clientPort;
            responseByte = quote.getBytes();

            //create and send response to client
            response = new DatagramPacket(responseByte, responseByte.length, clientIp, clientPort);
            ds.send(response);

            System.out.println("Message sent to Client: " +
                    "\nIP: " + clientIp +
                    "\nPort: " + clientPort +
                    "\nMessage: -" + quote + "\n");

            // Clear the buffers after every message.
            receive = new byte[65535];
            responseByte = new byte[65535];
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