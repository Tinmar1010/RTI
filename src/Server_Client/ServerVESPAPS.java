package Server_Client;

import java.io.IOException;
import java.sql.SQLException;

public class ServerVESPAPS implements Logger{
    ThreadServeur threadServeur;

    public ServerVESPAPS() throws IOException, SQLException, ClassNotFoundException
    {
        threadServeur = null;
    }

    public void start() throws IOException
    {
        Protocole protocole = null;
        protocole = new VESPAPS(this);
        threadServeur = new ThreadServeurPool(50500, protocole, 10, this);
        threadServeur.start();
        System.out.println("Serveur lancé") ;
    }


    @Override
    public void Trace(String message) {

    }
}
