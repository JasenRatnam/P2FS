package Handler;

import Requests.*;
import Responses.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.util.ArrayList;


import static java.lang.System.exit;

/**
 * class that handles requests from a client
 */
public class ClientHandler implements Runnable {

    // for receiving packets
    private DatagramPacket request;
    // for UDP connection
    private DatagramSocket ds;
    // buffer to store data received from a client
    byte[] dataBuffer;
    // thread to run request from client
    Thread threadClient;
    private static String log;

    /**
     * create a new thread with request from client
     * @param received data from client
     * @param cSocket where data is received from
     */
    public ClientHandler(DatagramPacket received, DatagramSocket cSocket) {
        this.request = received;
        this.ds = cSocket;

        threadClient = new Thread(this);
        threadClient.start();
    }

    /**
     * run client thread with request received
     */
    @Override
    public void run() {

        try {
            //request from client
            this.dataBuffer = request.getData();

            // transform serialized object back into the object
            ByteArrayInputStream byteStream = new ByteArrayInputStream(dataBuffer);
            ObjectInputStream input = new ObjectInputStream(byteStream);
            Object object = input.readObject();

            //print the object received
            // output to file
            Writer.receiveClient(object);

            //handle request from client
            requestHandler(object);

            //stop reading request
            input.close();
        } catch (IOException e) {
            //e.printStackTrace();
            log = "Receiver IOException " + e.getMessage();
            Writer.log(log);
            exit(1);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            log = "Cannot handle message from server: " + request.toString() + "\n";
            Writer.log(log);
        }
    }

    /**
     * Method to handle the request received from the client
     */
    public synchronized void requestHandler(Object requestInput){
        //add request to map of requests
        if (requestInput instanceof Request request1) {

            //get client IP and port number
            InetAddress clientIp = request.getAddress();
            String ip = clientIp.toString();
            int port = request.getPort();

            //store request with request number
            int RQNumb = request1.getRQNumb();
            String hashId = RQNumb + "-" + ip + ":" + port;
            Server.requestMap.put(hashId, requestInput);

            //if request is a register
            if (requestInput instanceof RegisterRequest) {
                register((RegisterRequest) requestInput);
            }
            //if request is a deregister
            else if (requestInput instanceof DeRegisterRequest) {
                deregister((DeRegisterRequest) requestInput);
            }
            else if (requestInput instanceof PublishRequest)
            {
                Publish((PublishRequest) requestInput);
            }
            else if(requestInput instanceof RemoveRequest)
            {
                RemoveFiles((RemoveRequest) requestInput);
            }
            else if(requestInput instanceof RetrieveAllRequest)
            {
                RetrieveAll((RetrieveAllRequest) requestInput);
            }
            else if(requestInput instanceof RetrieveInfoTRequest)
            {
                RetrieveInfo((RetrieveInfoTRequest) requestInput);
            }
            else if(requestInput instanceof SearchFileRequest)
            {
                SearchFile((SearchFileRequest) requestInput);
            }
            
            //need to add other requests
            else {
                log = "Cannot handle this request.";
                Writer.log(log);
            }
        }
    }

    private void RetrieveInfo(RetrieveInfoTRequest requestInput) {
        String clientIP = this.request.getAddress().getHostAddress();

        log = "Retreive InfoT request received\n";
        Writer.log(log);

        String errorCode = "";
        boolean registered = false;
        boolean nameExists = false;

        for ( ClientObject client: Server.clients) {
            //client is regsitered
            if (client.getIP().equals(clientIP)) {
                registered = true;
                break;
            }
        }

        // if not already registered
        if (!registered) {
            //ignore
        } else {
            //retreived
            //send retreive

            for (ClientObject client: Server.clients) {
                //client with name exists
                if (client.getName().equals(requestInput.getClientName())) {
                    nameExists = true;
                    RetrieveInfoT retrieveInfo = new RetrieveInfoT(requestInput.getRQNumb(),client.getName(),client.getIP(),client.getTCPport(),client.getFiles());
                    Sender.sendTo(retrieveInfo, this.request, ds);
                    log = "Client: " + clientIP + "has retrieved client:";
                    log += client.toString() + "\n";
                    break;
                }
            }
        }
        if (!nameExists) {
            //retreive denied
            errorCode = "Name not found";
            RetrieveError denied = new RetrieveError(requestInput.getRQNumb(),errorCode);
            Sender.sendTo(denied, this.request, ds);
            log = "Client: " + clientIP + " can not retreive because: " + errorCode;
        }
    }

    /**
     * Registration denied if the provided Name or IP is already in use
     * Need client unique name, IP Address, UDP socket#, TCP socket#
     * And request number
     */
    public void register(RegisterRequest request) {
        //save name of client to register
        String username = request.getClientName();
        //save ip of client to register
        String IP = request.getAddress();

        log = "Register request received\n";
        Writer.log(log);
        boolean error = false;
        String errorCode = null;

        //check if client is not already registered
        for ( ClientObject client: Server.clients) {
            if(client.getName().equals(username)){
                //registration denied
                log = "Registration Denied: name -" + username + " - already registered\n";
                error = true;
                errorCode = "Username already exists";
                Writer.log(log);
                break;
            }
            if(client.getIP().equals(IP)){
                //registration denied
                log = "Registration Denied: IP -" + IP + " - already registered\n";
                error = true;
                errorCode = "IP address already exists";
                Writer.log(log);
                break;
            }
        }
        // if not already registered
        if (!error) {
            //register
            Server.clients.add(request.getClientObject());
            Writer.makeServerBackup();
            //Send confirmation too client
            RegisterConfirmed confirmation = new RegisterConfirmed(request.getRQNumb());
            Sender.sendTo(confirmation, this.request, ds);
            log = "Client: " + username + " at " + IP + " has been registered";
        } else {
            //registration denied
            RegisterDenied denied = new RegisterDenied(errorCode, request.getRQNumb());
            Sender.sendTo(denied, this.request, ds);
            log = "Client: " + username + " at " + IP + " has not been registered because: " + errorCode;
        }
        log += "\nServer has " + Server.clients.size() + " client(s)\n";

        Writer.log(log);

        // remove request from list of request
        remove(request.getRQNumb(), this.request);
    }

    /**
     * DeRegister client with given name
     * if no client exists with given name DeRegister
     */
    public void deregister(DeRegisterRequest request) {
        //save name of client
        String username = request.getClientName();

        log = "Deregister request received\n";
        Writer.log(log);

        boolean deregister = false;
        int i = 0;

        for ( ClientObject client: Server.clients) {
            if(client.getName().equals(username)){
                //deregister client
                deregister = true;
                Server.clients.remove(i);
                Writer.makeServerBackup();
                log = client + " has been DeRegistered.\n";
                log += "\nServer has " + Server.clients.size() + " client(s)\n";
                break;
            }
            i++;
        }
        if(!deregister)
        {
            //can't deregister
            log = username + " cannot be DeRegistered.";
            log += "\nCannot find " + username  + "\n";
            log += "\nServer has " + Server.clients.size() + " client(s)\n";
        }
        Writer.log(log);

        // remove request from list of request
        remove(request.getRQNumb(), this.request);
    }


    public void Publish(PublishRequest request)
    {
        //save name of client to register
        String username = request.getClientName();

        ArrayList<String> listOfFile = request.getListOfFiles();
        log = "Publish request received\n";
        Writer.log(log);
        boolean Published = false;
        String errorCode = null;

// match the client with the clients in the server
        for ( ClientObject client: Server.clients) {
            if (client.getName().equals(username)) {
                Published = true;
                client.setFiles(listOfFile);
                log = username + " has published files successfully. \n";
                log += "Published: \n" + request.getListOfFiles() + "\n";
                Writer.log(log);
                break;

            }
        }

        // if not already registered
        if (!Published) {
            //published denied
            errorCode = "User not found";
            PublishDenied denied = new PublishDenied(errorCode, request.getRQNumb());
            Sender.sendTo(denied, this.request, ds);
            log = "Client: " + username + " can not publish because: " + errorCode;
        } else {
            //published
            //send published
            PublishConfirmed confirmation = new PublishConfirmed(request.getRQNumb());
            Sender.sendTo(confirmation, this.request, ds);
            log = "Client: " + username + "has published files";

        }
    }

    public void RemoveFiles(RemoveRequest request) {
        String username = request.getClientName();

        log = "Remove files request received\n";
        Writer.log(log);
        String errorCode = "";
        boolean removed = false;
        
        ArrayList<String> listOfFileToRemove = request.getListOfFiles();
        
        // match the client with the clients in the server
        for (ClientObject client : Server.clients) {
            if (client.getName().equals(username)) {
                if(contains(client.getFiles(),listOfFileToRemove))
                {
                    client.removeFile(listOfFileToRemove);
                    removed=true;
                    log = username + " has removed files successfully. \n";
                    log += "Removed: \n" + request.getListOfFiles() + "\n";
                    log += "Current client files \n: " +  client.getFiles() + "\n";
                    Writer.log(log);
                    break;
                }else {
                    errorCode = "File not found\n";
                    log = username + " cannot remove files." + "\n";
                    log += " file does not exist" + "\n";
                    Writer.log(log);
                }
            }
            else{
                log = username + " cannot remove files.";
                log += "\nCannot find " + username  + "\n";
                log += "\nServer has " + Server.clients.size() + " client(s)\n";
                Writer.log(log);
                errorCode += "User not found\n";
            }
        }

        if(!removed)
        {
            //can't remove
            //send a response
            //send Remove-denied
            RemoveDenied denied = new RemoveDenied(errorCode, request.getRQNumb());
            Sender.sendTo(denied, this.request, ds);
        }
        else{
            //send Removed
            RemoveConfirmed confirmation = new RemoveConfirmed(request.getRQNumb());
            Sender.sendTo(confirmation, this.request, ds);
        }

        // remove request from list of request
        remove(request.getRQNumb(), this.request);
    }

    boolean contains(ArrayList<String> list, ArrayList<String> sublist) {
        boolean contains = true;
        int l1 = list.size(), l2 = sublist.size();
        int currIndex = 0;
        int i;
        for(int j=0;j<l2;j++) {
            String e2 = sublist.get(j);
            for(i=currIndex;i<l1;i++) {
                if(e2.equals(list.get(i))) {
                    break;
                }
            }
            if(i == l1) {
                contains = false;
                break;
            }
            currIndex++;
        }
        return  contains;
    }


    public  void RetrieveAll(RetrieveAllRequest request)
    {
        String clientIP = this.request.getAddress().getHostAddress();

        log = "Retreive All request received\n";
        Writer.log(log);
        String errorCode = "";
        boolean retreived = false;

        for ( ClientObject client: Server.clients) {
            //client is regsitered
            if (client.getIP().equals(clientIP)) {
                retreived = true;
                 break;
            }
        }

        // if not already registered
         if (!retreived) {
             //ignore
         } else {
             //retreived
             //send retreive
             log = "The registered users" + "\n";
             for ( ClientObject client: Server.clients) {
                 log += client.getName() + " " + client.getIP() + " " + client.getTCPport() + " " + client.getFiles() + "\n" ;
             }
             Writer.log(log);

             Retrieve retrieveAll = new Retrieve(request.getRQNumb(),Server.clients);
             Sender.sendTo(retrieveAll, this.request, ds);
             log = "Client: " + clientIP + "has retreived clients";
       }
    }

    public void SearchFile(SearchFileRequest request)
    {
        ArrayList<ClientObject> FoundClients = new ArrayList<>();;
        String filename = request.getFilename();
        log = " Search File request received\n";

        Writer.log(log);
        String errorCode = "";
        boolean fileFound = false;

        for ( ClientObject client: Server.clients) {
            //to see if client has file
            if (client.getFiles().contains(filename)) {
                fileFound = true;
                log = filename + " has been found successfully with :\n";
                log += client.getName() + " " + client.getIP() + " " + client.getTCPport() + "\n" ;
                FoundClients.add(client);
                Writer.log(log);
            }
        }
        if (!fileFound) {
            //file not found
            errorCode = "File not found with a user(s)";
            SearchError notfound = new SearchError(request.getRQNumb(),errorCode);
            Sender.sendTo(notfound, this.request, ds);
            log = "file: " + filename + " can not retreive because: " + errorCode;
            Writer.log(log);
        }
        SearchFileResponse found = new SearchFileResponse(request.getRQNumb(), FoundClients);
        Sender.sendTo(FoundClients,this.request, ds);
    }
    /**
     * Remove request from the list of requests
     */
    public static void remove(int rid, DatagramPacket packet) {
        //client ip and port
        String ip = packet.getAddress().toString();
        int port = packet.getPort();
        String hashId = rid + "-" + ip + ":" + port;

        //remove request
        Server.requestMap.remove(hashId);
    }
}