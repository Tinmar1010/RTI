package Client_Achat.protocol;

import java.io.IOException;
import java.net.SocketException;
import java.util.StringTokenizer;

public class OVESP {
        private final Libsocket serverConnection;
    public OVESP(String ip, int port) throws IOException{
        this.serverConnection = new Libsocket(ip, port);
    }
    public int OVESPLogin(String username, String Password, boolean newUserFlag) throws IOException {
        serverConnection.SendMsg("LOGIN#" + username + "#" + Password + "#" + (newUserFlag ? "1": "0") + "#");
        String[] response = serverConnection.Receive_msg().split("#");

        if (response[0].equals("LOGIN")) {
            if (response[1].trim().equals("BAD_REQUEST"))
                return -3;
            else if (response[1].trim().equals("OK"))
                return 0;
            else if (response[1].trim().equals("KO")) {
                if (response[2].trim().equals("BAD_USER"))
                    return 1;
                else if (response[2].trim().equals("BAD_PASS"))
                    return 2;
                else if (response[2].trim().equals("DB_FAIL"))
                    return 3;
                else if (response[2].trim().equals("ALREADY_EXISTS"))
                    return 4;
                else
                    return -4;
            }
        }
        else
            return -4;

        return 0;
    }
}
