package Server_Client.Client;

import Server_Client.Requete;

import java.io.Serializable;

public class RequeteLOGIN implements Requete, Serializable
{
    private String login;
    private String password;
    private boolean isnew;

    public RequeteLOGIN(String l, String p, boolean iznew)
    {
        login = l;
        password = p;
        isnew = iznew;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isIsnew() {
        return isnew;
    }
}
