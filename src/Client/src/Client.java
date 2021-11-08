import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import static java.lang.System.exit;

/**
 * Main class thread
 */
public class Client {
    public static InetAddress serverIp;
    public static InetAddress clientIp;
    public static int serverPort;
    public static int clientUDPPort;
    public static int clientTCPPort = 3000;
    private static DatagramSocket ds;
    private static Scanner sc = new Scanner(System.in);
    public static AtomicInteger requestCounter = new AtomicInteger(0);
    public static String ClientName;
    public static ConcurrentHashMap<Integer,Object> requestMap = new ConcurrentHashMap<>();
    public static boolean isRegistered = false;
    private static String log;

    /**
     * constructor of a client
     */
    public Client() {
        clientConfig();
    }

    /**
     * configure server on startup
     */
    public void clientConfig() {
        try {
            //ask and get IP of server
            System.out.println("Enter IP address of the Server: ");
            String ip = sc.nextLine();
            serverIp = InetAddress.getByName(ip);

            //ask and get port of server
            System.out.println("Enter port number of the Server: ");
            String port = sc.nextLine();
            serverPort = Integer.parseInt(port);

            //connect to UDP socket
            ds = new DatagramSocket();
            clientIp = InetAddress.getLocalHost();
            clientUDPPort = ds.getLocalPort();

            //client information
            log = "Client information: " +
                    "\nIP: " +  clientIp +
                    "\nUDPPort: " + clientUDPPort +
                    "\nTCPPort: " + clientTCPPort;
            log(log);

        } catch (SocketException ex) {
            log  = "Socket error: " + ex.getMessage();
            log += "\nClosing client.... ";
            log(log);
            ds.close();
            exit(1);
        } catch(UnknownHostException uhEx) {
            log = "HOST ID not found.. ";
            log += "\nClosing client.... ";
            log(log);
            ds.close();
            exit(1);
        }
    }

    /**
     * start running the client
     */
    public void start() {
        //threading listening to any message from the server.
        ServerHandler receiver = new ServerHandler(ds);
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();

        //run UI of the client and get commands to send to server
        ui();
    }

    /**
     * UI of the client
     * displays commands to server and handles them
     */
    public static void ui() {
        String val = "";
        while (!val.equals("exit")) {
            System.out.println("\nEnter 'exit' to close client");
            System.out.println("Possible commands:");
            System.out.println("1-Register");
            // need to add more commands

            System.out.println("Enter number of wanted command: ");
            val = sc.nextLine();

            if (val.isEmpty()) {
                val = "-1";
            }
            switch (val) {
                case "1":
                    log = "User selected Register";
                    register(sc);
                    break;
                case "-1":
                    log = "User selected Nothing";
                    continue;
                default:
                    log = "User selected a not valid option";
                    continue;
            }
            log(log);
        }
    }

    /**
     * Clients selects the register command
     * send request to server
     */
    public static void register(Scanner s) {
        //get name of client
        System.out.print("\tEnter Username to register: ");
        ClientName = s.next();

        try {
            //create a register request
            RegisterRequest registerMessage = new RegisterRequest(requestCounter.incrementAndGet(), ClientName,
                    clientIp.toString(), clientUDPPort, clientTCPPort);
            //send request to server
            Sender.sendTo(registerMessage, ds, Client.serverIp.getHostAddress(), Client.serverPort);
            log = registerMessage.toString();
            log += "\nMessage sent to: " + serverIp + ": " + serverPort + "\n";
            log(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String logText)
    {
        try {
            Writer.log(logText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
