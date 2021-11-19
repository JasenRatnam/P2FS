package Handler;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.exit;

/**
 * Main server thread and implementation
 */
public class Server implements Runnable {

    private static DatagramSocket ds;
    private static String log;
    private static InetAddress serverIp;
    public static int serverPort;
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
                //e.printStackTrace();
                log  = "Socket error: " + ex.getMessage();
                log += "\nTry again..\n ";
                Writer.log(log);
                ds.close();
                exit(1);
            } catch (IOException ex) {
                //e.printStackTrace();
                log = "IOException " + ex.getMessage();
                Writer.log(log);
                exit(1);
            }
        }
    }

    /**
     * configure server on startup
     */
    public void serverConfig() {

        //if backup exists, reload settings from backup.csv
        //if no backup start from scratch
        String backupPath = "backup.csv";
        File f = new File(backupPath);
        if(!f.isFile()) {
            //if no backup exists
            //ask and get port of server
            serverPort = getPort();
        }
        else{
            //if a backup exists
            //read backup file and save them in the server

            //read backup
            Writer.restoreServer();
        }

        //continue asking until valid socket is received
        while (true) {
            try {
                log = "opening port..... \n";
                Writer.log(log);
                ds = new DatagramSocket(serverPort);
                break;
            } catch (SocketException ex) {
                ds.close();
                log = "SocketException: " + ex.getMessage();
                Writer.log(log);
                //ask and get port of server

                //get another port number
                serverPort = getPort();
            }
        }

        Writer.makeServerBackup();

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

        Writer.log(log);
    }

    public static int getPort( ){
        int port = 0;
        //ask and get port of server
        System.out.println("Enter port number of the server: (1-65535)");
        while (!sc.hasNextInt())
        {
            sc.next(); // Read and discard offending non-int input
            System.out.println("Please enter a valid port number: (1-65535) "); // Re-prompt
            System.out.println("Enter port number of the server: (1-65535)");
        }
        port = sc.nextInt();

        // Ports should be between 0 - 65535
        while(port < 1 || port > 65535) {
            System.out.println("Port out of range: 1-65535");
            System.out.println("Enter port number of the server: (1-65535)");
            while (!sc.hasNextInt())
            {
                sc.next(); // Read and discard offending non-int input
                System.out.print("Please enter a valid port number: (1-65535) "); // Re-prompt
            }
            port = sc.nextInt();
        }
        sc.nextLine();
        return port;
    }
}