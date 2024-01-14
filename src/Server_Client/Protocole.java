package Server_Client;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.SQLException;

public interface Protocole
{
    String getNom();
    Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException, SQLException, ClassNotFoundException, IOException, NoSuchAlgorithmException, NoSuchProviderException, CertificateException, KeyStoreException, SignatureException, InvalidKeyException, UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException;

}