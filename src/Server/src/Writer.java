import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Write log to a external file
 */
public class Writer {

    private static String Timestamp;
    private static String fileName = "Server.txt";

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

            str.append("\n----------------NEW MESSAGE From Client--------------------");
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
     * log any string to the file
     * @param log
     * @throws IOException
     */
    public static void log(String log) throws IOException {

        Timestamp = new Date().toString();

        try(BufferedWriter br = new BufferedWriter(new FileWriter(fileName,true))){
            StringBuilder str = new StringBuilder();

            str.append("\n-------------------------------------------------");
            System.out.println(str);
            br.write(str.toString());
            br.newLine();
            br.write(log);
            System.out.print(log);
            br.newLine();
            br.write(Timestamp);
            System.out.println(Timestamp);
            br.newLine();
            br.write(str.toString());
            br.newLine();
        }
    }

    /**
     * add the object sent to a client from server
     * @param object
     * @param clientAddress
     * @param clientPort
     * @throws IOException
     */
    public static void sendResponse(Object object, String clientAddress, int clientPort) throws IOException {
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
            br.write("Sending to ->" + clientAddress + ":" + clientPort);
            System.out.println("Sending to ->" + clientAddress + ":" + clientPort);
            br.newLine();
            br.write(Timestamp);
            System.out.println(Timestamp);
            br.newLine();
        }
    }
}