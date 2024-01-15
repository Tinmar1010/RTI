import Server_Client.ServerVESPAPS;
import WebServer.MaraicherWeb;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.Security;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException
    {
        MaraicherWeb maraicherWeb = new MaraicherWeb();

//        Security.addProvider(new BouncyCastleProvider());
//        ServerVESPAPS ServerVESPAPS = new ServerVESPAPS();
//        ServerVESPAPS.start();
//        PaiementSecureGUI fenetre = new PaiementSecureGUI();
//        ControleurPaiementSecure controleur = new ControleurPaiementSecure (fenetre);
//        fenetre.setControleur(controleur);

    }
}