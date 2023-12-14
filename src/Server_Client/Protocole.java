package Server_Client;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public interface Protocole
{
    String getNom();
    Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException, SQLException, ClassNotFoundException, IOException;

}