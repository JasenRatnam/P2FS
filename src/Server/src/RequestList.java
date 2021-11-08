import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class RequestList {

    /**
     * Add Request received to list of requests
     * @param request
     * @param packet
     */
    public static void received(Object request, DatagramPacket packet) {
        //add the request only if it is a type of known request
        if (request instanceof Request) {
            Request request1 = (Request) request;

            //get client IP and port number
            InetAddress clientIp = packet.getAddress();
            String ip = clientIp.toString();
            int port = packet.getPort();

            //store request with request number
            int RQNumb = request1.getRQNumb();
            String hashId = RQNumb + "-" + ip + ":" + port;
            Server.requestMap.put(hashId, request);
        }
    }

    /**
     * Remove request from the list of requests
     * @param rid
     * @param packet
     */
    public static void remove(int rid, DatagramPacket packet) {
        try {
            //client ip and port
            String ip = packet.getAddress().getLocalHost().getHostAddress();
            int port = packet.getPort();
            String hashId = rid + "-" + ip + ":" + port;

            //remove request
            Request req = (Request) Server.requestMap.get(hashId);
            Server.requestMap.remove(hashId);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}