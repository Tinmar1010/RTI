package Server_Client.Server;

import Server_Client.Reponse;

import java.io.Serializable;

public class ReponseLOGIN implements Serializable, Reponse
{
    private boolean ok;

    public ReponseLOGIN(boolean o)
    {
        ok = o;
    }

    public boolean isOk() {
        return ok;
    }
}
