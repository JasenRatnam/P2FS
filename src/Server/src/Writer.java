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

    public static void appendToFile(Object object) throws IOException {

        Timestamp = new Date().toString();

        try(BufferedWriter br = new BufferedWriter(new FileWriter(fileName,true))){
            StringBuilder str = new StringBuilder();

            str.append("----------------NEW MESSAGE From Client--------------------");
            br.write(str.toString());
            br.newLine();
            br.write(object.toString());
            br.newLine();
            br.write(Timestamp);
            br.newLine();
        }
    }

    public static void log(String log) throws IOException {

        System.out.println(log);
        Timestamp = new Date().toString();

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

    public static void sendResponse(Object object, String clientAddress, int clientPort) throws IOException {
        Timestamp = new java.util.Date().toString();
        try (BufferedWriter br = new BufferedWriter(new FileWriter(fileName, true))) {
            // System.out.println("Writer "+ object.toString());
            StringBuilder str = new StringBuilder();

            str.append("----------------SENDING-------------");
            br.write(str.toString());
            br.newLine();
            br.write(object.toString());
            br.newLine();
            br.write("Sending to ->" + clientAddress + ":" + clientPort);
            br.newLine();
            br.write(Timestamp);
            br.newLine();
        }
    }
}