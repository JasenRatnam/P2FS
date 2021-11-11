import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Timestamp;
import java.util.Date;

/**
 * Write log to a external file
 */
public class Writer {

    private static String Timestamp;
    private static String fileName = "Client.txt";

    /**
     * append an object to the text file
     * @param object
     * @throws IOException
     */
    public static void appendToFile(Object object) throws IOException {
        //get current timestamp
        Timestamp = new Date().toString();

        //add object to file with a timestamp
        try(BufferedWriter br = new BufferedWriter(new FileWriter(fileName,true))){
            StringBuilder str = new StringBuilder();

            str.append("\n----------------NEW MESSAGE From Server--------------------");
            System.out.println(str);
            br.write(str.toString());
            br.newLine();
            br.write(object.toString());
            System.out.println(object.toString());
            br.newLine();
            br.write(Timestamp);
            System.out.println(Timestamp);
            br.newLine();
        }
    }

    /**
     * add object sent to server
     * @param object
     * @param serverAddress
     * @param serverPort
     * @throws IOException
     */
    public static void sendRequest(Object object, String serverAddress, int serverPort) throws IOException {
        Timestamp = new java.util.Date().toString();
        try (BufferedWriter br = new BufferedWriter(new FileWriter(fileName, true))) {
            StringBuilder str = new StringBuilder();

            str.append("\n----------------SENDING-------------");
            br.write(str.toString());
            System.out.println(str);
            br.newLine();
            br.write(object.toString());
            System.out.println(object.toString());
            br.newLine();
            br.write("Sending to ->" + serverAddress + ":" + serverPort);
            System.out.println("Sending to ->" + serverAddress + ":" + serverPort);
            br.newLine();
            br.write(Timestamp);
            System.out.println(Timestamp);
            br.newLine();
        }
    }

    /**
     * log any string to the file
     * @param log
     * @throws IOException
     */
    public static void log(String log) throws IOException {

        System.out.print(log);
        Timestamp = new Date().toString();
        System.out.println(Timestamp + "\n");

        try(BufferedWriter br = new BufferedWriter(new FileWriter(fileName,true))){
            StringBuilder str = new StringBuilder();

            str.append("-------------------------------------------------");
            br.write(str.toString());
            br.newLine();
            br.write(log);
            br.newLine();
            br.write(Timestamp);
            br.newLine();
            br.write(str.toString());
            br.newLine();
        }
    }
}