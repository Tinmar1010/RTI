package Server_Client;

import java.io.IOException;
import java.sql.SQLException;

public class ServerVESPAP implements Logger{
    ThreadServeur threadServeur;

    public ServerVESPAP() throws IOException, SQLException, ClassNotFoundException
    {
        threadServeur = null;
    }

    public void start() throws IOException
    {
        Protocole protocole = null;
        protocole = new VESPAP(this);
        threadServeur = new ThreadServeurPool(50000, protocole, 10, this);
        threadServeur.start();
        System.out.println("Serveur lancé") ;
    }


    @Override
    public void Trace(String message) {

    }
}
