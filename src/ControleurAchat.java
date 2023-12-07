import Client_Achat.protocol.Libsocket;
import Client_Achat.protocol.OVESP;
import Client_Achat.protocol.OVESPConnectionError;
import Server_Client.Client.RequeteLOGIN;
import Server_Client.Client.RequeteLOGOUT;
import Server_Client.Server.ReponseLOGIN;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ControleurAchat implements ActionListener, WindowListener
{
    private final GUIMaraicherEnLigne fenetre;
    private OVESP ovespConnection;
    public ControleurAchat(GUIMaraicherEnLigne fenetre) throws IOException {
        this.fenetre = fenetre;
        try {
            this.ovespConnection = new OVESP("localhost", 4444);
            fenetre.setStatusSuccess("Connecte ! BIENVENUE SUR LE MARAICHER EN LIGNE !");
        }
        catch(SocketException se) {
            fenetre.setStatusError("Connection impossible vers le serveur");
        }

    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("Login"))
        {
            try {
                int connectionStatus = ovespConnection.OVESPLogin(fenetre.getLogin(), fenetre.getPassword(), fenetre.getCheckBox());
                if (connectionStatus == 0) {
                    JOptionPane.showMessageDialog(fenetre, "Bienvenue " + fenetre.getLogin());
                }
                else if (connectionStatus == 1) {
                    JOptionPane.showMessageDialog(fenetre, "Erreur, le nom d'utilisateur n'existe pas");
                }
                else if (connectionStatus == 2) {
                    JOptionPane.showMessageDialog(fenetre, "Le mot de passe est incorrect !");
                }
                else if (connectionStatus == 3) {
                    JOptionPane.showMessageDialog(fenetre, "Une erreur de base de donnee est survenue !");
                }
                else if (connectionStatus == 4) {
                    JOptionPane.showMessageDialog(fenetre, "Le nom d'utilisateur existe deja !");
                }
                else if (connectionStatus == -1) {
                    JOptionPane.showMessageDialog(fenetre, "Connection avec le serveur perdue !");
                }
                else if (connectionStatus >= -2) {
                    JOptionPane.showMessageDialog(fenetre, "Une erreur interne au serveur est survenue !");
                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
        if(e.getActionCommand().equals("Logout"))
        {

        }
        if(e.getActionCommand().equals("Supprimer"))
        {

        }
        if(e.getActionCommand().equals("Vider"))
        {

        }
        if(e.getActionCommand().equals("AchatTot"))
        {

        }
        if(e.getActionCommand().equals("Achat"))
        {

        }
        if(e.getActionCommand().equals("PrevArticle"))
        {

        }
        if(e.getActionCommand().equals("NextArticle"))
        {

        }

    }

    @Override
    public void windowOpened(WindowEvent e){}
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