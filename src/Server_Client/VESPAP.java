package Server_Client;

import BDaccess.VESPAP_BD;
import Classe_Metier.Factures;
import Server_Client.Client.*;
import Server_Client.Server.*;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

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
            return TraiteRequeteLOGIN((RequeteLOGIN) requete);

        if (requete instanceof RequeteLOGOUT)
            return (Reponse) TraiteRequeteLOGOUT((RequeteLOGOUT) requete);

        if(requete instanceof RequeteGETFACTURES)
            return TraiteRequeteFactures((RequeteGETFACTURES) requete);

        if(requete instanceof RequetePAYFACTURE)
            return TraiteRequetePayerFactures((RequetePAYFACTURE) requete);
        return null;
    }

    private synchronized ReponseLOGIN TraiteRequeteLOGIN(RequeteLOGIN requete) throws FinConnexionException, SQLException, ClassNotFoundException, IOException
    {
        logger.Trace("RequeteLOGIN reçue de " + requete.getLogin());
        vespapBd = new VESPAP_BD("MySql", "localhost", "PourStudent", "Student", "PassStudent1_");
        int rep = vespapBd.Login(requete.getLogin(), requete.getPassword(), requete.isIsnew());
        if(rep == -1 || rep ==-2)
            return new ReponseLOGIN(false);
        else
        {
            return new ReponseLOGIN(true);
        }
    }
    private synchronized ReponseLOGOUT TraiteRequeteLOGOUT(RequeteLOGOUT requete) throws FinConnexionException
    {
        return new ReponseLOGOUT();

    }
    private synchronized ReponseGETFACTURES TraiteRequeteFactures(RequeteGETFACTURES requete) throws FinConnexionException, SQLException, ClassNotFoundException
    {
        logger.Trace("RequeteGETFactures ");
        vespapBd = new VESPAP_BD("MySql", "localhost", "PourStudent", "Student", "PassStudent1_");
        ArrayList <Factures> listeFactures = new ArrayList<Factures>();
        listeFactures = vespapBd.Get_Factures(parseInt(requete.getIDClient()));
        if(listeFactures == null)
            return new ReponseGETFACTURES(false, listeFactures);
        else
            return new ReponseGETFACTURES(true, listeFactures);

    }

    private synchronized ReponsePAYFACTURE TraiteRequetePayerFactures(RequetePAYFACTURE requete) throws FinConnexionException, SQLException, ClassNotFoundException
    {
        logger.Trace("RequetePayFactures ");
        boolean isOK = AlgoLuhn(requete.getNumeroCarte());
        if(isOK)
        {
            int rep = vespapBd.Pay_Facture(requete.getIdFacture());
            if(rep ==1)
                return new ReponsePAYFACTURE(true);
            else
                return new ReponsePAYFACTURE(false);
        }
        else
            return new ReponsePAYFACTURE(false);
    }

    private boolean AlgoLuhn(String NumVisa)
    {
        int nDigits = NumVisa.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--)
        {

            int d = NumVisa.charAt(i) - '0';

            if (isSecond == true)
                d = d * 2;

            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }
}
