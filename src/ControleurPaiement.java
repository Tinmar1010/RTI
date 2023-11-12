import Server_Client.Client.RequeteLOGIN;
import Server_Client.Server.ReponseLOGIN;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ControleurPaiement implements ActionListener, WindowListener
{
    private final PaiementGUI fenetre;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket socket;
    public ControleurPaiement(PaiementGUI fenetre) throws IOException {
        this.fenetre = fenetre;
        initComponents();
        oos = null;
        ois = null;

    }

    private void initComponents() {
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

                ReponseLOGIN reponse = null;
                System.out.println(reponse);
                reponse= (ReponseLOGIN) ois.readObject();
                if(reponse.isOk())
                    System.out.println("Success !!");
                else
                    System.out.println("Error");
            }
            catch (IOException | ClassNotFoundException ex)
            {
                System.out.println("Probleme dans le controleur" + ex.getMessage());
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