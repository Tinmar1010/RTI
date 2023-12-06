import Client_Achat.protocol.Libsocket;
import Server_Client.ServerVESPAP;

import java.io.IOException;
import java.nio.Buffer;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException
    {
        ServerVESPAP ServerVESPAP = new ServerVESPAP();
        ServerVESPAP.start();
        GUIMaraicherEnLigne fenetre = new GUIMaraicherEnLigne();
        ControleurAchat controleur = new ControleurAchat (fenetre);

        fenetre.setControleur(controleur);
    }
}