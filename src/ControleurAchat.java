import Classe_Metier.Articles;
import Client_Achat.protocol.OVESP;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ControleurAchat implements ActionListener, WindowListener
{
    private final GUIMaraicherEnLigne fenetre;
    private int currentArticle = 1;
    private OVESP ovespConnection;
    public ControleurAchat(GUIMaraicherEnLigne fenetre) throws IOException {
        this.fenetre = fenetre;
        this.ovespConnection = new OVESP();
        fenetre.setStatusError("Veuillez vous identifiez");
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("Login"))
        {
            LoginHandler();
        }
        if(e.getActionCommand().equals("Logout"))
        {
            if (fenetre.isBucketEmpty() != 0) {
                CancelAll();
            }
            fenetre.clearEntries();
            fenetre.clearArticle();
            fenetre.setGuiLogout();
            fenetre.setStatusError("Veuillez vous identifiez");
            try {
                ovespConnection.OVESP_Disconnect();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
        if(e.getActionCommand().equals("Supprimer"))
        {
            if (fenetre.isBucketEmpty() == 0) {
                JOptionPane.showMessageDialog(fenetre, "Le panier est vide !");
            }
            else if (fenetre.isArticleSelected() == -1) {
                JOptionPane.showMessageDialog(fenetre, "Veuillez dabord selectionner un article a supprimer !");
            }
            else {
                int CancelError = CancelHandler(fenetre.getSelectedArticle());
                if (CancelError == 0) {
                    ConsultHandler(2);
                    fenetre.clearEntries();
                    CaddieHandler();
                }
            }
        }
        if(e.getActionCommand().equals("Vider"))
        {
            CancelAll();
        }
        if(e.getActionCommand().equals("CONFIRM"))
        {
            ConfirmerHandler();
        }
        if(e.getActionCommand().equals("Achat"))
        {
            AchatHandler();
            fenetre.clearEntries();
            CaddieHandler();
        }
        if(e.getActionCommand().equals("PrevArticle"))
        {
                ConsultHandler(0);

        }
        if(e.getActionCommand().equals("NextArticle"))
        {
            ConsultHandler(1);
        }

    }
    private void ConsultHandler(int prev_next) {
        try {
            int ArticleError;

            if (prev_next == 1)
                ArticleError = ovespConnection.OVESPConsult(++currentArticle);
            else if (prev_next == 0)
                ArticleError = ovespConnection.OVESPConsult(--currentArticle);
            else
                ArticleError = ovespConnection.OVESPConsult(currentArticle);
            if (ArticleError == 1) {
                JOptionPane.showMessageDialog(fenetre, "L'article n'existe pas !");
                if (prev_next == 1)
                    --currentArticle;
                else if (prev_next == 0)
                    ++currentArticle;
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
            ovespConnection.OVESP_Connect("localhost", 4444);
            fenetre.setStatusSuccess("Connecte ! BIENVENUE SUR LE MARAICHER EN LIGNE !");
        }
        catch(IOException se) {
            fenetre.setStatusError("Connection impossible vers le serveur");
        }
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
        ConsultHandler(1);
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
            int CaddieError = ovespConnection.OVESPCaddie();
            if (CaddieError == 1 || CaddieError == -1)
                JOptionPane.showMessageDialog(fenetre, "Erreur fatale !");

            else if (CaddieError == 0) {
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

    public int CancelHandler(Articles art) {
        int cancelError;

        if (art != null) {
            try {
                cancelError = ovespConnection.OVESPCancel(art.getId(), art.getStock());
                if (cancelError == -2) {
                    JOptionPane.showMessageDialog(fenetre, "Une erreur interne est survenue !");
                    return 1;
                }
                else
                    return 0;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            JOptionPane.showMessageDialog(fenetre, "Une erreur interne est survenue !");
            return 1;
        }

    }
    public void CancelAll() {
        int CancelError = 0;

        List<Articles> articlesList;

        if (fenetre.isBucketEmpty() == 0) {
            JOptionPane.showMessageDialog(fenetre, "Le panier est vide !");
        }
        else {
            articlesList = fenetre.getAllArticles();
            for (Articles art : articlesList) {
                if (CancelHandler(art) == -1)
                    CancelError = -1;
            }
            ConsultHandler(2); // Consult currrent to update quantity
            fenetre.clearEntries();     // Clear the bucket
            if (CancelError == -1) { // If an error occured during the cancellation, we update the Bucket
                CaddieHandler();
            }


        }
    }

    public void ConfirmerHandler(){
        int confirmerError;
        try {
        confirmerError = ovespConnection.OVESPConfirmer(fenetre.getLogin());
        if (confirmerError == -1)
            JOptionPane.showMessageDialog(fenetre, "La confirmation n'a pas pu se faire !");
        else if (confirmerError == 1)
            JOptionPane.showMessageDialog(fenetre, "le panier est vide !");
        else
            fenetre.clearEntries();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
     @Override
    public void windowOpened(WindowEvent e){}
    @Override
    public void windowClosing(WindowEvent e) {
        if (ovespConnection.isSocketAlive()) {
            if (fenetre.isBucketEmpty() != 0) {
                CancelAll();
            }
            try {
                ovespConnection.OVESP_Disconnect();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
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