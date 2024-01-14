import Classe_Metier.Factures;
import Server_Client.Client.*;
import Server_Client.Server.ReponseGETFACTURES;
import Server_Client.Server.ReponseLOGIN;
import Server_Client.Server.ReponsePAYFACTURE;

import javax.crypto.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Date;
import Crypto.*;

public class ControleurPaiementSecure implements ActionListener, WindowListener
{
    private final PaiementSecureGUI fenetre;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket socket;

    public  ControleurPaiementSecure(PaiementSecureGUI fenetre) throws IOException {
        this.fenetre = fenetre;
        socket = new Socket("127.0.0.0",50500);
        oos = null;
        ois = null;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("Login"))
        {
            try
            {
                //digest salé + envoi d'un clé de session
                socket = new Socket("127.0.0.0",50500);
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                if(fenetre.getLogin().isEmpty()||fenetre.getPassword().isEmpty())
                {
                    JOptionPane.showMessageDialog(fenetre, "Veuillez entrer votre login ou votre mdp");
                }
                else
                {
                    //Génération de la clé de session
                    KeyGenerator cleGen = KeyGenerator. getInstance("DES","BC");
                    cleGen.init(new SecureRandom());
                    SecretKey cleSession = cleGen.generateKey();
                    System.out.println("Clé coté client " + cleSession);

                    //Récupération de la clé publique pour pouvoir crypter le message
                    PublicKey clePubliqueServ = RecupereClePubliqueServeur();

                    //Cryptage de la clé de session avec la clé publique du server
                    byte[] cleSessionCrypte;
                    cleSessionCrypte = MyCrypto.CryptAsymRSA(clePubliqueServ,cleSession.getEncoded());


                    //Construction de la requete + envoie au serveur
                    RequeteLOGINSecure requete = new RequeteLOGINSecure(fenetre.getLogin(), fenetre.getPassword(), fenetre.getCheckBox(), cleSessionCrypte);
                    oos.writeObject(requete);
                    ReponseLOGIN reponse= (ReponseLOGIN) ois.readObject();
                    System.out.println(reponse.toString());
                    if(reponse.isOk())
                        fenetre.isConnected();
                    else
                        JOptionPane.showMessageDialog(fenetre, "Erreur de login");
                }

            }
            catch (IOException | ClassNotFoundException ex) {
                System.out.println("Probleme dans le controleur : " + ex.getMessage());
            } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException |
                     NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getActionCommand().equals("Logout"))
        {
            try
            {
                RequeteLOGOUT requete = new RequeteLOGOUT();
                oos.close();
                ois.close();
                socket.close();
                fenetre.isDeconnected();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getActionCommand().equals("Show"))
        {
            try {
                fenetre.supprimerLigne();
                RequeteGETFACTURES requete = new RequeteGETFACTURES(fenetre.getIdClient());
                oos.writeObject(requete);
                ReponseGETFACTURES reponse = (ReponseGETFACTURES)ois.readObject();
                if(reponse.isOk()) {
                    ArrayList<Factures> factures;
                    factures = reponse.getListeFactures();
                    int i = 0;
                    while(i<factures.size())
                    {
                        fenetre.addFactures(factures.get(i));
                        i++;
                    }
                }
                else
                    JOptionPane.showMessageDialog(fenetre, "Erreur d'achat");
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getActionCommand().equals("Payer"))
        {
            JDialog Dialog = new JDialog(fenetre, "");
            JLabel label = new JLabel("Entrez votre numéro de compte:");
            JTextField numeroCompteTextField = new JTextField(20);
            JButton payerButton = new JButton("Payer");

            // Ajout des composants à la fenêtre
            Dialog.setLayout(new BorderLayout());
            Dialog.add(label, BorderLayout.NORTH);
            Dialog.add(numeroCompteTextField, BorderLayout.CENTER);
            Dialog.add(payerButton, BorderLayout.SOUTH);
            Dialog.setSize(400, 300);
            Dialog.setVisible(true);
            payerButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        if(fenetre.RecupId()!=-1)
                        {
                            RequetePAYFACTURE requete = new RequetePAYFACTURE(fenetre.RecupId(), numeroCompteTextField.getText());
                            oos.writeObject(requete);
                            ReponsePAYFACTURE reponse = (ReponsePAYFACTURE) ois.readObject();
                            if (reponse.isOk())
                                JOptionPane.showMessageDialog(fenetre, "Paiement Ok");
                            else
                                JOptionPane.showMessageDialog(fenetre, "Erreur de paiement");
                        }
                    }
                    catch (IOException | ClassNotFoundException ex)
                    {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }
    }
    public static PublicKey RecupereClePubliqueServeur()
    {
        KeyStore ks = null;
        PublicKey cle = null;
        try {
            ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("keystoreServeurMartin.jks"),"Test123".toCharArray());
            X509Certificate certif = (X509Certificate)ks.getCertificate("Martincle");
            cle = certif.getPublicKey();
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return cle;
    }


    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
}