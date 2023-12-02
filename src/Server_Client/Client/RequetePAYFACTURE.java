package Server_Client.Client;

import Server_Client.Requete;

import java.io.Serializable;

public class RequetePAYFACTURE  implements Requete, Serializable {

    private int IdFacture;
    private String NumeroCarte;

    public RequetePAYFACTURE(int ID, String Num)
    {
        IdFacture = ID;
        NumeroCarte = Num;
    }

    public int getIdFacture() {
        return IdFacture;
    }

    public String getNumeroCarte() {
        return NumeroCarte;
    }

    public void setIdFacture(int idFacture) {
        IdFacture = idFacture;
    }

    public void setNumeroCarte(String numeroCarte) {
        NumeroCarte = numeroCarte;
    }
}
