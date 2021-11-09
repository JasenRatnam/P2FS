import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main server thread and implementation
 */
public class Server implements Runnable {

    private static DatagramSocket ds;
    private static String log;
    private static InetAddress serverIp;
    private static int serverPort;
    private static DatagramPacket request;
    private static byte[] receive = null;
    private static final Scanner sc = new Scanner(System.in);
    public static ConcurrentHashMap<String,Object> requestMap = new ConcurrentHashMap<>();
    public static ArrayList<ClientObject> clients = new ArrayList<>();
    ClientHandler clientHandler;

    /**
     * constructor that configs the server
     */
    public Server() {
        serverConfig();
    }

    /**
     * thread running the server
     */
    @Override
    public void run() {
        //array to store message received
        receive = new byte[65535];

        //keep reading messages
        while (true) {
            //receive request gotten from a client
            request = new DatagramPacket(receive, receive.length);
            try {
                ds.receive(request);

                // create a new thread for each request
                clientHandler = new ClientHandler(request, ds);

            } catch (SocketTimeoutException ex) {
                log = "SocketTimeoutException: " + ex.getMessage();
                log(log);
            } catch (IOException ex) {
                log = "IOException " + ex.getMessage();
                log(log);
            }
        }
    }

    /**
     * configure server on startup
     */
    public void serverConfig() {

        //ask and get port of server
        System.out.println("Enter port number of the Server: ");
        String port = sc.nextLine();
        serverPort = Integer.parseInt(port);

        //continue asking until valid socket is received
        while (true) {
            try {
                log = "opening port..... \n";
                log(log);
                ds = new DatagramSocket(serverPort);
                break;
            } catch (SocketException ex) {
                ds.close();
                log = "SocketException: " + ex.getMessage();
                log(log);
                //ask and get port of server
                System.out.println("Enter port number of the Server: ");
                port = sc.nextLine();
                serverPort = Integer.parseInt(port);
            }
        }

        //get IP of server
        try {
            serverIp = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //print information of the server
        log = "Server information: " +
                "\nIP: " +  serverIp.toString() +
                "\nPort: " + serverPort + "\n";

        log(log);
    }

    public void log(String logText)
    {
        try {
            Writer.log(logText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
