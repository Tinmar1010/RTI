package Server_Client.Client;

import Server_Client.Requete;

import java.io.Serializable;

public class RequeteGETFACTURES implements Requete, Serializable {

    private String IDClient;

    public RequeteGETFACTURES(String id)
    {
        IDClient = id;
    }

    public void setIDClient(String IDClient) {
        this.IDClient = IDClient;
    }
    public String getIDClient(){return IDClient;}
}
