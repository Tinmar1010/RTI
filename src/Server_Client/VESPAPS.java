package Server_Client;

import BDaccess.VESPAP_BD;
import Classe_Metier.Factures;
import Server_Client.Client.*;
import Server_Client.Server.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.ArrayList;
import Crypto.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static java.lang.Integer.parseInt;

public class VESPAPS implements Protocole {
    private VESPAP_BD vespapBd;
    private Logger logger;
    public VESPAPS(Logger log){

        logger = log;
    }
    @Override
    public String getNom() {
        return "VESPAPS";
    }

    @Override
    public synchronized Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException, SQLException, ClassNotFoundException, IOException, NoSuchAlgorithmException, NoSuchProviderException, CertificateException, KeyStoreException, SignatureException, InvalidKeyException, UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        if (requete instanceof RequeteLOGINSecure)
            return TraiteRequeteLOGINSecure((RequeteLOGINSecure) requete);

        if (requete instanceof RequeteLOGOUT)
            return (Reponse) TraiteRequeteLOGOUT((RequeteLOGOUT) requete);

        if(requete instanceof RequeteGETFACTURES)
            return TraiteRequeteFactures((RequeteGETFACTURES) requete);

        if(requete instanceof RequetePAYFACTURE)
            return TraiteRequetePayerFactures((RequetePAYFACTURE) requete);
        return null;
    }
    public static PrivateKey RecupereClePriveeServeur() throws KeyStoreException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException {


        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("keystoreServeurMartin.jks"),"Test123".toCharArray());
        return (PrivateKey) ks.getKey("Martincle","Test123".toCharArray());
    }
    private synchronized ReponseLOGIN TraiteRequeteLOGINSecure(RequeteLOGINSecure requete) throws FinConnexionException, SQLException, ClassNotFoundException, IOException, NoSuchAlgorithmException, NoSuchProviderException, CertificateException, KeyStoreException, SignatureException, InvalidKeyException, UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        //Digest salé + envoi session clé
        logger.Trace("RequeteLOGIN reçue de " + requete.getLogin());


        // Recuperation de la clé privée du serveur
        PrivateKey clePriveeServeur = RecupereClePriveeServeur();

        // Decryptage asymétrique de la clé de session
        byte[] cleSessionDecryptee;
        cleSessionDecryptee = MyCrypto.DecryptAsymRSA(clePriveeServeur,requete.getClesession());
        SecretKey cleSession = new SecretKeySpec(cleSessionDecryptee,"DES");
        System.out.println("Clé coté server " + cleSession);

        vespapBd = new VESPAP_BD("MySql", "localhost", "PourStudent", "Student", "PassStudent1_");

        if(requete.isIsnew())
        {
            int rep = vespapBd.Login(requete.getLogin(), requete.getPassword(), requete.isIsnew());
            if(rep == -1 || rep ==-2)
                return new ReponseLOGIN(false);
            else
            {
                return new ReponseLOGIN(true);
            }
        }
        else
        {
            String mdp = vespapBd.get_the_password(requete.getLogin());
            if (mdp == null) {
                System.out.println("Client inconnu !");
                System.exit(0);
                return new ReponseLOGIN(false);
            }
            else
            {
                if(requete.VerifyPassword(mdp))
                    return new ReponseLOGIN(true);
                else
                    return new ReponseLOGIN(false);
            }
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
