package Handler;

import Requests.DeRegisterRequest;
import Requests.PublishRequest;
import Requests.RegisterRequest;
import Requests.RemoveRequest;

import java.net.*;
import java.util.ArrayList;
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
    private static final Scanner sc = new Scanner(System.in);
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
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

        try {
            //ask and get IP of server
            System.out.println("Enter IP address of the Server: ");
            String ip = sc.nextLine();

            while (!ip.matches(PATTERN)){
                System.out.println("Please enter a valid IP address");
                System.out.println("Enter IP address of the Server: ");
                ip = sc.nextLine();
            }
            serverIp = InetAddress.getByName(ip);

            //ask and get port of server
            System.out.println("Enter port number of the Server: (1-65535)");
            while (!sc.hasNextInt())
            {
                sc.next(); // Read and discard offending non-int input
                System.out.println("Please enter a valid port number: (1-65535) "); // Re-prompt
            }
            serverPort = sc.nextInt();

            // Ports should be between 0 - 65535
            while(serverPort < 1 || serverPort > 65535) {
                System.out.println("Port out of range: 1-65535");
                System.out.println("Enter port number of the Server: (1-65535)");
                while (!sc.hasNextInt())
                {
                    sc.next(); // Read and discard offending non-int input
                    System.out.print("Please enter a valid port number: (1-65535) "); // Re-prompt
                }
                serverPort = sc.nextInt();
            }

            //connect to UDP socket
            ds = new DatagramSocket();
            clientIp = InetAddress.getLocalHost();
            clientUDPPort = ds.getLocalPort();

            //client information
            log = "\nClient information: " +
                    "\nIP: " +  clientIp +
                    "\nUDP Port: " + clientUDPPort +
                    "\nTCP Port: " + clientTCPPort + "\n";

            //server information
            log += "\n\nServer information: " +
                    "\nIP: " +  serverIp +
                    "\nPort: " + serverPort + "\n";
            Writer.log(log);

        } catch (SocketException ex) {
            //e.printStackTrace();
            log  = "Socket error: " + ex.getMessage();
            log += "\nClosing client.... ";
            Writer.log(log);
            ds.close();
            exit(1);
        } catch(UnknownHostException uhEx) {
            //e.printStackTrace();
            log = "HOST ID not found.... ";
            log += "\nClosing client....\n ";
            Writer.log(log);
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
        while (!val.equals("exit") || isRegistered) {
            System.out.println("\nEnter 'exit' to close client");
            System.out.println("""
                    Possible commands:
                    1-Register
                    2-Deregister
                    3-Publish
                    4-Remove
                    5-Retrieve All
                    6-Retrieve infoT
                    7-Search file
                    8-Download
                    9-Update contact
                    10-Disconnect""");

            System.out.println("Enter number of the wanted command:");
            val = sc.nextLine();


            if (val.isEmpty()) {
                val = "-1";
            }


            switch (val) {
                case "1":
                    //has to register with the server before publishing or discovering what
                    // is available for share.
                    if(isRegistered) {
                        log = "User is already Registered\n";
                        Writer.log(log);
                        continue;
                    }
                    else{
                        log = "User selected Register\n";
                        Writer.log(log);
                        register(sc);
                        break;
                    }
                case "2":
                    log = "User selected DeRegister\n";
                    Writer.log(log);
                    deregister(sc);
                    break;
                case "3":
                    //A registered client can publish and retrieve information
                    // about available files and where to download them from.
                    if(isRegistered) {
                        log = "User selected Publish\n";
                        Writer.log(log);
                        //publish(sc);
                        break;
                    }
                    else{
                        log = "Please register first\n";
                        Writer.log(log);
                        continue;
                    }
                case "4":
                    //remove a file (or a list of files) from its offering,
                    if(isRegistered) {
                        log = "User selected Remove\n";
                        Writer.log(log);
                       // remove(sc);
                        break;
                    }
                    else{
                        log = "Please register first\n";
                        Writer.log(log);
                        continue;
                    }
                case "5":
                    //A registered user can retrieve information from the server
                    // by sending different kinds of requests.
                    //A user can retrieve for instance the names of all the other registered clients,
                    if(isRegistered) {
                        log = "User selected Retrieve All\n";
                        Writer.log(log);
                        break;
                    }
                    else{
                        log = "Please register first\n";
                        Writer.log(log);
                        continue;
                    }
                case "6":
                    //A registered user can also request the information about a specific peer.
                    if(isRegistered) {
                        log = "User selected Retrieve infoT\n";
                        Writer.log(log);
                        break;
                    }
                    else{
                        log = "Please register first\n";
                        Writer.log(log);
                        continue;
                    }
                case "7":
                    //A user can search for a specific file
                    if(isRegistered) {
                        log = "User selected Search file\n";
                        Writer.log(log);
                        break;
                    }
                    else{
                        log = "Please register first\n";
                        Writer.log(log);
                        continue;
                    }
                case "8":
                    //set a TCP connection to the peer,
                    log = "User selected Download\n";
                    Writer.log(log);
                    break;
                case "9":
                    //A registered user can always modify his/her IP address,
                    // UDP socket#, and/or TCP socket#
                    log = "User selected Update contact\n";
                    Writer.log(log);

                    break;
                case "10":
                    log = "User selected Disconnect\n";
                    log += "Disconnecting...\n";
                    Writer.log(log);
                    exit(1);
                    break;
                case "-1":
                    log = "User selected Nothing\n";
                    Writer.log(log);
                    continue;
                default:
                    log = "User selected an invalid option\n";
                    Writer.log(log);
            }
        }
        exit(1);
    }

    /**
     * Clients selects the register command
     * send request to server
     */
    public static void register(Scanner s) {
        //get name of client
        System.out.print("\tEnter Username to register: ");
        String name = s.next();
        ClientName=name;
        //create a register request
        RegisterRequest registerMessage = new RegisterRequest(requestCounter.incrementAndGet(), name,
                clientIp.toString(), clientUDPPort, clientTCPPort);
        //send request to server
        Sender.sendTo(registerMessage, ds, Client.serverIp.getHostAddress(), Client.serverPort);
    }

    /**
     * Clients selects the deregister command
     * send request to server
     * give name to deregister
     */
    public static void deregister(Scanner s) {

        //get name of client
        System.out.print("\tEnter Username to deregister: ");
        String name = s.next();

        DeRegisterRequest deregisterMessage = new DeRegisterRequest(Client.requestCounter.incrementAndGet(),
                name);

        if(name.equals(ClientName)) {
            isRegistered = false;
            System.out.println("You have been DeRegistered.\n");
        }

        Sender.sendTo(deregisterMessage, ds, Client.serverIp.getHostAddress(), Client.serverPort);
    }

    public static void publish(Scanner s) {
        String filename = "";
        ArrayList<String> listOfFile = new ArrayList<String>();
        while(filename != "exit") {
            System.out.print("\tPlease enter the name of the files you wish to publish(to exit, please write exit): ");
            filename = s.next();
            if(filename !="exit") {
                listOfFile.add(s.next());
            }
        }
        PublishRequest publishMessage = new PublishRequest(requestCounter.incrementAndGet(),ClientName,listOfFile);


    }

    public static void remove(Scanner s) {
        String filename = "";
        ArrayList<String> listOfFileToRemove = new ArrayList<String>();
        while(filename != "exit") {
            System.out.print("\tPlease enter the name of the files you wish to publish(to exit, please write exit): ");
            filename = s.next();
            if(filename !="exit") {
                listOfFileToRemove.add(s.next());
            }
        }

        RemoveRequest removeMessage = new RemoveRequest(requestCounter.incrementAndGet(),ClientName,listOfFileToRemove);

    }

}