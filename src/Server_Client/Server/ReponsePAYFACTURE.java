package Server_Client.Server;

import Server_Client.Reponse;

import java.io.Serializable;

public class ReponsePAYFACTURE implements Reponse, Serializable {

    private boolean ok;

    public ReponsePAYFACTURE(boolean o)
    {
        ok = o;
    }

    public boolean isOk() {
        return ok;
    }
}
