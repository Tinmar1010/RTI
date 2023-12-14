import Classe_Metier.Factures;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PaiementGUI extends JFrame {
    private JTextField LoginText;
    private JPasswordField PasswordText;
    private JButton loginButton;
    private JButton logoutButton;
    private JCheckBox isNewCheckBox;
    private JTextField IDClientText;
    private JTable table1;
    private JButton showButton;
    private JButton payerButton;
    private JLabel Login;
    private JLabel IDClient;
    private JPanel Panel1;
    private DefaultTableModel model;
    String[] Column = {"ID facture", "ID Client", "Date", "Montant", "Payé"};

    public PaiementGUI() {

        model = new DefaultTableModel(null, Column);
        table1.setModel(model);
        setContentPane(Panel1);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        loginButton.setActionCommand("Login");
        logoutButton.setActionCommand("Logout");
        showButton.setActionCommand("Show");
        payerButton.setActionCommand("Payer");
        showButton.setEnabled(false);
        payerButton.setEnabled(false);
        logoutButton.setEnabled(false);
    }

    public void setControleur(ControleurPaiement c) {
        loginButton.addActionListener(c);
        logoutButton.addActionListener(c);
        showButton.addActionListener(c);
        payerButton.addActionListener(c);

        this.addWindowListener(c);
    }

    public String getLogin() {
        return LoginText.getText();
    }

    public String getPassword() {
        return PasswordText.getText();
    }

    public String getIdClient() {
        return IDClientText.getText();
    }

    public boolean getCheckBox() {
        if (isNewCheckBox.isSelected())
            return true;
        else
            return false;
    }
    public void isConnected()
    {
        showButton.setEnabled(true);
        payerButton.setEnabled(true);
        logoutButton.setEnabled(true);
        loginButton.setEnabled(false);
    }
    public void isDeconnected()
    {
        showButton.setEnabled(false);
        payerButton.setEnabled(false);
        logoutButton.setEnabled(false);
        loginButton.setEnabled(true);
    }

    public void addFactures(Factures factures) {
        model.addRow(new Object[]{factures.getIdFactures(), factures.getIdClient(), factures.getDate(), factures.getMontant(), factures.isPaye()});
    }

    public void supprimerLigne() {
        model.setRowCount(0);

    }
    public int RecupId()
    {
        int line = table1.getSelectedRow();
        int rep = (int)table1.getValueAt(line, 0);
        if(rep==-1) {
            JOptionPane.showMessageDialog(this, "Pas de facture selectionnée");
            return rep;
        }
        else
            return rep;
    }
}
