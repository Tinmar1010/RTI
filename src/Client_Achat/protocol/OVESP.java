package Client_Achat.protocol;

import java.io.IOException;
import java.net.SocketException;
import java.util.*;

public class OVESP {
        private final Libsocket serverConnection;
        private ArrayList<String> current_request;

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

    public int OVESPConsult(int article) throws IOException {
        ArrayList<String> request = new ArrayList<String>();
        request.add("CONSULT");
        request.add(Integer.toString(article));
        serverConnection.SendMsg(createRequest(request));

        String[] response = serverConnection.Receive_msg().split("#");

        /* README : Le consult rate care le sreveur est mal code, il ne renvoie pas de CONSULT avant le -1 pour dire
        que ca nexiste pas. Le meme probleme apparait en C++, quelque chose a du changer sur le github. ca renvoie que -1
        en cas d'erreur et pas CONSULT#-1...
         */
        if (response[0].equals("CONSULT")) {
            if (response[1].equals("-1")) {
                return 1;
            }
            else {
                current_request = new ArrayList<String>();
                Collections.addAll(current_request, response);
                return 0;
            }
        }
        else {
            return -1;
        }

    }
    private String createRequest(ArrayList<String> tokens) {
        StringBuilder request = new StringBuilder();
        for (String s: tokens) {
            request.append(s).append("#");
        }
        return request.toString();
    }
    public ArrayList<String> getResponse() {
        return current_request;
    }

}
