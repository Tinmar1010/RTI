import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PaiementGUI extends JFrame{
    private JTextField LoginText;
    private JPasswordField PasswordText;
    private JButton loginButton;
    private JButton LogoutButton;
    private JCheckBox isNewCheckBox;
    private JTextField IDClientText;
    private JTable table1;
    private JButton achatButton;
    private JButton payerButton;
    private JLabel Login;
    private JLabel IDClient;
    private JPanel Panel1;
    private DefaultTableModel model;

    String[] Column = {"ID facure", "Date", "Montant", "Payé"};

    public PaiementGUI()
    {
        setContentPane(Panel1);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        loginButton.setActionCommand("Login");
    }

    public void setControleur(ControleurPaiement c) {
        loginButton.addActionListener(c);
        LogoutButton.addActionListener(c);
        achatButton.addActionListener(c);
        payerButton.addActionListener(c);

        this.addWindowListener(c);
    }
    public String getLogin(){return LoginText.getText();}
    public String getPassword(){return PasswordText.getText();}
    public boolean getCheckBox()
    {
        if(isNewCheckBox.isSelected())
            return true;
        else
            return false;
    }


}
