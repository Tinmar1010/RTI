import Classe_Metier.Factures;
import Server_Client.Client.RequeteGETFACTURES;
import Server_Client.Client.RequeteLOGIN;
import Server_Client.Client.RequeteLOGOUT;
import Server_Client.Client.RequetePAYFACTURE;
import Server_Client.Server.ReponseGETFACTURES;
import Server_Client.Server.ReponseLOGIN;
import Server_Client.Server.ReponsePAYFACTURE;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ControleurPaiement implements ActionListener, WindowListener
{
    private final PaiementGUI fenetre;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket socket;
    public ControleurPaiement(PaiementGUI fenetre) throws IOException {
        this.fenetre = fenetre;
        socket = new Socket("127.0.0.0",50000);
        oos = null;
        ois = null;
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("Login"))
        {
            try
            {
                socket = new Socket("127.0.0.0",50000);
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                RequeteLOGIN requete = new RequeteLOGIN(fenetre.getLogin(), fenetre.getPassword(), fenetre.getCheckBox());
                oos.writeObject(requete);
                ReponseLOGIN reponse= (ReponseLOGIN) ois.readObject();
                if(reponse.isOk())
                    fenetre.isConnected();
                else
                    JOptionPane.showMessageDialog(fenetre, "Erreur de login");
            }
            catch (IOException | ClassNotFoundException ex)
            {
                System.out.println("Probleme dans le controleur : " + ex.getMessage());
            }
        }
        if(e.getActionCommand().equals("Logout"))
        {

            try {
                RequeteLOGOUT requete = new RequeteLOGOUT();
                oos.close();
                ois.close();
                socket.close();
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
                    JOptionPane.showMessageDialog(fenetre, "Erreur d'achat");
                }
                else
                    System.out.println("Error");
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getActionCommand().equals("Payer"))
        {
            try
            {
                JDialog jDialog = new JDialog(fenetre, "Entrez votre numero de compte");
                JTextField t = new JTextField();
                jDialog.add(t);
                jDialog.setSize(400, 200);
                jDialog.setVisible(true);
                RequetePAYFACTURE requete = new RequetePAYFACTURE(fenetre.RecupId(), t.getText());
                oos.writeObject(requete);
                ReponsePAYFACTURE reponse = (ReponsePAYFACTURE)ois.readObject();
                if(reponse.isOk())
                {

                }
                else
                {
                    JOptionPane.showMessageDialog(fenetre, "Erreur de paiement");

                }
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }

        }
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