//socket with UDP
import Handler.Client;

import java.io.IOException;

import java.net.UnknownHostException;


/**
 * Main class of the Client
 * starts a client thread
 */
public class Main {

    /**
     * Main method
     * run client in a thread
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.start();
    }
}