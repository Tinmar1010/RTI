import Classe_Metier.Articles;
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
import java.util.ArrayList;
import java.util.ListIterator;

public class ControleurAchat implements ActionListener, WindowListener
{
    private final GUIMaraicherEnLigne fenetre;
    private int currentArticle = 1;
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
            LoginHandler();
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
            AchatHandler();
            fenetre.clearEntries();
            CaddieHandler();
        }
        if(e.getActionCommand().equals("PrevArticle"))
        {
                ConsultHandler(false);

        }
        if(e.getActionCommand().equals("NextArticle"))
        {
            ConsultHandler(true);
        }

    }
    private void ConsultHandler(boolean prev_next) {
        try {
            int ArticleError;

            if (prev_next)
                ArticleError = ovespConnection.OVESPConsult(++currentArticle);
            else
                ArticleError = ovespConnection.OVESPConsult(--currentArticle);
            if (ArticleError == 1) {
                if (prev_next) {
                    JOptionPane.showMessageDialog(fenetre, "L'article n'existe pas !");
                    --currentArticle;
                }
                else {
                    JOptionPane.showMessageDialog(fenetre, "L'article n'existe pas !");
                    ++currentArticle;
                }
            }
            else if (ArticleError == -1) {
                JOptionPane.showMessageDialog(fenetre, "Une erreur interne au serveur est survenue !");
            }
            else {
                ArrayList<String> response = ovespConnection.getResponse();
                Articles curr_article = new Articles(currentArticle, Integer.parseInt(response.get(4).trim()), Float.parseFloat(response.get(3).trim()), response.get(5).trim());
                curr_article.setIntitule(response.get(2).trim());
                fenetre.setArticle(curr_article);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void LoginHandler() {
        try {
            int connectionStatus = ovespConnection.OVESPLogin(fenetre.getLogin(), fenetre.getPassword(), fenetre.getCheckBox());
            if (connectionStatus == 0) {
                JOptionPane.showMessageDialog(fenetre, "Bienvenue " + fenetre.getLogin());
                fenetre.setGuiLogin();
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
        currentArticle = 0;
        ConsultHandler(true);
    }

    public void AchatHandler() {
        try {
            int AchatError = ovespConnection.OVESPAchat(currentArticle, Integer.parseInt(fenetre.getQuantityField()));
            if (AchatError == 1)
                JOptionPane.showMessageDialog(fenetre, "L'article n'existe pas !");
            else if (AchatError == 2)
                JOptionPane.showMessageDialog(fenetre, "La quantite est trop elevee !");
            else if (AchatError == 3)
                JOptionPane.showMessageDialog(fenetre, "La quantite ne peux pas valoir 0 !");
            else if (AchatError == -2)
                JOptionPane.showMessageDialog(fenetre, "Une erreur interne est survenue !");
            else if (AchatError == 0) {
                ArrayList<String> response = ovespConnection.getResponse();
                Articles curr_article = new Articles(currentArticle, fenetre.getStockField() - Integer.parseInt(response.get(4).trim()), Float.parseFloat(response.get(3).trim()), response.get(5).trim());
                curr_article.setIntitule(response.get(2).trim());
                fenetre.setArticle(curr_article);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void CaddieHandler()
    {
        try {
            int AchatError = ovespConnection.OVESPCaddie();
            if (AchatError == 1 || AchatError == -1)
                JOptionPane.showMessageDialog(fenetre, "Erreur fatale !");

            else if (AchatError == 0) {
                ArrayList<String> response = ovespConnection.getResponse();
                ListIterator<String> it = response.listIterator();
                while (it.hasNext()) {
                    if (it.next().trim().equals("CADDIE")) {
                        Articles curr_article = new Articles(Integer.parseInt(it.next()),it.next(), Float.parseFloat(it.next()), Integer.parseInt(it.next()), it.next());
                        fenetre.addArticle(curr_article);
                    }
                }

            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
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