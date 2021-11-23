//socket with UDP
import Handler.Client;

/**
 * Main class of the Client
 * starts a client thread
 */
public class Main {

    /**
     * Main method
     * run client in a thread
     */

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}