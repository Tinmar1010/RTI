package Server_Client.Server;

import Classe_Metier.Factures;
import Server_Client.Reponse;

import java.io.Serializable;
import java.util.ArrayList;

public class ReponseGETFACTURES implements Reponse, Serializable {
    private boolean ok;
    private ArrayList<Factures> listeFactures;
    public ReponseGETFACTURES(boolean o, ArrayList<Factures>LF)
    {
        ok = o;
        listeFactures = LF;
    }
    public boolean isOk() {
        return ok;
    }

    public ArrayList<Factures> getListeFactures() {
        return listeFactures;
    }
}
