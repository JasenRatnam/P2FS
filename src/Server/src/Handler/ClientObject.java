package Handler;

import java.util.ArrayList;

public class ClientObject {

    private String name;
    private String IP;
    private int UDPport;
    private int TCPport;
    public ArrayList<String> files = new ArrayList<>();


    /**
     * constructor of a client object
     * @param name of client
     * @param IP of client
     * @param UDPport of client to talk to server
     * @param TCPport of client to talk with other clients
     */
    public ClientObject(String name, String IP, int UDPport, int TCPport) {
        this.name = name;
        this.IP = IP;
        this.UDPport = UDPport;
        this.TCPport = TCPport;
    }

    /**
     * String of the client
     */
    @Override
    public String toString() {
        String str = "Client{" +
                "name='" + name + '\'' +
                ", IP='" + IP + '\'' +
                ", UDPport=" + UDPport +
                ", TCPport=" + TCPport;
        if(files != null) {
            str += "}\nFiles: {" +
            String.join(", ", files)  + "}";
        }
        else{
            str += "}";
        }
        return str;
    }

    public void addFile(String file) {
        files.add(file);
        Writer.makeServerBackup();
    }

    public void removeFile(String file) {
        files.remove(file);
        Writer.makeServerBackup();
    }

    //getters and setters
    public ArrayList<String> getFiles() {
        return files;
    }

    //getters and setters
    public String getFilesString() {
        String str = "";
        for(int i=0;i<files.size();i++)
        {
            str += "," + files.get(i);
        }
        return str;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
        Writer.makeServerBackup();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        Writer.makeServerBackup();
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
        Writer.makeServerBackup();
    }

    public int getUDPport() {
        return UDPport;
    }

    public void setUDPport(int UDPport) {
        this.UDPport = UDPport;
        Writer.makeServerBackup();
    }

    public int getTCPport() {
        return TCPport;
    }

    public void setTCPport(int TCPport) {
        this.TCPport = TCPport;
        Writer.makeServerBackup();
    }
}
