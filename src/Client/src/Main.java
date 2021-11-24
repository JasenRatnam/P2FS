//socket with UDP
import Handler.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

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
        //new GUI();
        Client client = new Client();
        client.start();


    }
}