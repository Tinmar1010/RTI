package Client_Achat.protocol;

import java.io.IOException;
import java.net.SocketException;
import java.util.*;

public class OVESP {
        private final Libsocket serverConnection;

    public OVESP(String ip, int port) throws IOException{
        this.serverConnection = new Libsocket(ip, port);
    }
    public int OVESPLogin(String username, String Password, boolean newUserFlag) throws IOException {
        ArrayList<String> request = new ArrayList<String>();
        request.add("LOGIN");
        request.add(username);
        request.add(Password);
        request.add((newUserFlag ? "1" : "0"));

        serverConnection.SendMsg(createRequest(request));
        String[] response = serverConnection.Receive_msg().split("#");

        if (response[0].equals("LOGIN")) {
            switch (response[1].trim()) {
                case "BAD_REQUEST" -> {
                    return -3;
                }
                case "OK" -> {
                    return 0;
                }
                case "KO" -> {
                    return switch (response[2].trim()) {
                        case "BAD_USER" -> 1;
                        case "BAD_PASS" -> 2;
                        case "DB_FAIL" -> 3;
                        case "ALREADY_EXISTS" -> 4;
                        default -> -4;
                    };
                }
            }
        }
        else
            return -4;

        return 0;
    }
    private String createRequest(ArrayList<String> tokens) {
        StringBuilder request = new StringBuilder();
        for (String s: tokens) {
            request.append(s).append("#");
        }
        return request.toString();
    }
}
