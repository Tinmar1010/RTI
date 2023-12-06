import Client_Achat.protocol.Libsocket;
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

public class ControleurAchat implements ActionListener, WindowListener
{
    private final GUIMaraicherEnLigne fenetre;
    private final Libsocket server_socket = null;
    public ControleurAchat(GUIMaraicherEnLigne fenetre) throws IOException {
        this.fenetre = fenetre;
        //this.server_socket = new Libsocket("localhost", 4444);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("Login"))
        {

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