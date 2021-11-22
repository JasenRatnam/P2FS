package Handler;

import java.io.*;
import java.util.Date;


/**
 * Write log to an external file
 */
public class Writer {

    private static String Timestamp;
    private static final String fileName = "Server.txt";
    private static final  String backup = "backup.csv";

    /**
     * append an object to the text file
     */
    public static void receiveClient(Object object){
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
            System.out.println(object);
            br.newLine();
            br.write(Timestamp);
            System.out.println(Timestamp);
            br.newLine();
        }catch (IOException e) {
            //e.printStackTrace();
            String log = "IOException.... ";
            log += "\nFile logging failed....\n ";
            Writer.log(log);
        }
    }

    /**
     * log any string to the file
     */
    public static void log(String log){

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
        } catch (IOException e) {
            //e.printStackTrace();
            String log2 = "IOException.... ";
            log2 += "\nFile logging failed....\n ";
            Writer.log(log2);
        }
    }

    /**
     * add the object sent to a client from server
     */
    public static void sendResponse(Object object, String clientAddress, int clientPort){
        Timestamp = new java.util.Date().toString();
        try (BufferedWriter br = new BufferedWriter(new FileWriter(fileName, true))) {
            StringBuilder str = new StringBuilder();

            str.append("\n----------------SENDING-------------");
            br.write(str.toString());
            System.out.println(str);
            br.newLine();
            br.write(object.toString());
            System.out.println(object);
            br.newLine();
            br.write("Sending to ->" + clientAddress + ":" + clientPort);
            System.out.println("Sending to ->" + clientAddress + ":" + clientPort);
            br.newLine();
            br.write(Timestamp);
            System.out.println(Timestamp);
            br.newLine();
        }catch (IOException e) {
            //e.printStackTrace();
            String log = "IOException.... ";
            log += "\nFile logging failed....\n ";
            Writer.log(log);
        }
    }

    /**
     * create a backup of the server
     * save server port selection
     * save clients registerd to server
     */
    public static void makeServerBackup() {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(backup, false))) {
            br.write("Server UDP Port");
            br.newLine();
            br.write(String.valueOf(Server.serverPort));
            br.newLine();
            br.write("Name,IP,UDPport,TCPport,Filenames");
            br.newLine();
            for(int i=0;i<Server.clients.size();i++)
            {
                br.write(Server.clients.get(i).getName() + "," +
                             Server.clients.get(i).getIP() + "," +
                             Server.clients.get(i).getUDPport() + "," +
                             Server.clients.get(i).getTCPport() +
                             Server.clients.get(i).getFilesString()
                        );
                br.newLine();
            }
        }catch (IOException e) {
            //e.printStackTrace();
            String log = "IOException.... ";
            log += "\nServer backup failed....\n ";
            Writer.log(log);
        }
    }

    /**
     * restore the server from saved data
     * set port of the server
     * add all clients saves with their files
     */
    public static void restoreServer() {
        String delimiter = ",";
        StringBuilder log;

        try (BufferedReader br = new BufferedReader(new FileReader(backup))) {
            log = new StringBuilder("Restoring server...\n");

            String line;
            //remove server port header
            br.readLine();

            //ge port of server
            Server.serverPort = Integer.parseInt(br.readLine());

            //remove client header
            br.readLine();

            //read and save clients
            while((line = br.readLine()) != null){
                String[] values = line.split(delimiter);
                String name = values[0];
                String IP = values[1];
                int UDPport = Integer.parseInt(values[2]);
                int TCPport = Integer.parseInt(values[3]);

                ClientObject client = new ClientObject(name, IP, UDPport, TCPport);

                //add all filenames of client
                for(int i=4;i<values.length;i++) {
                    String filenamex = values[i];
                    client.addFile(filenamex);
                }

                Server.clients.add(client);
                log.append("Restored client: \n").append(client).append("\n");
            }
            log(log.toString());
        } catch (IOException e){
            //e.printStackTrace();
            log = new StringBuilder("IOException.... ");
            log.append("\nServer restore failed....\n ");
            Writer.log(log.toString());
        }
    }
}