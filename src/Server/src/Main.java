//socket with UDP
import java.io.IOException;

/**
 * Main class of the Server
 * starts a server thread
 */
public class Main {

    /**
     * Main method
     * run server in a thread
     * @param args
     * @throws IOException
     */
    public static void main(String[] args){
         Server server = new Server();
         Thread serverThread = new Thread(server);
         serverThread.start();
    }
}