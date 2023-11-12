package Server_Client;

import BDaccess.VESPAP_BD;
import Server_Client.Client.RequeteLOGIN;
import Server_Client.Client.RequeteLOGOUT;
import Server_Client.Server.ReponseLOGIN;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class VESPAP implements Protocole {
    private VESPAP_BD vespapBd;
    private Logger logger;

    public VESPAP(Logger log){

        logger = log;
    }


    @Override
    public String getNom() {
        return "VESPAP";
    }

    @Override
    public synchronized Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException, SQLException, ClassNotFoundException, IOException
    {

        if (requete instanceof RequeteLOGIN)
        {
            return TraiteRequeteLOGIN((RequeteLOGIN) requete, socket);
        }

        if (requete instanceof RequeteLOGOUT)
            return null;

        return null;
    }

    private synchronized ReponseLOGIN TraiteRequeteLOGIN(RequeteLOGIN requete, Socket socket) throws FinConnexionException, SQLException, ClassNotFoundException, IOException {
        logger.Trace("RequeteLOGIN reçue de " + requete.getLogin());
        vespapBd = new VESPAP_BD("MySql", "localhost", "PourStudent", "Student", "PassStudent1_");
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        int rep = vespapBd.Login(requete.getLogin(), requete.getPassword(), requete.isIsnew());
        if(rep == -1 || rep ==-2)
            return new ReponseLOGIN(false);
        else
        {
            System.out.println("Success");
            return new ReponseLOGIN(true);
        }


    }
    private synchronized void TraiteRequeteLOGOUT(RequeteLOGOUT requete) throws FinConnexionException
    {

    }
}
