import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GUIMaraicherEnLigne extends JFrame{
    private JPanel panel1;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton logoutButton;
    private JCheckBox isnewCheckBox;
    private JTable table1;
    private JButton validerButton;
    private JButton supprimerButton;
    private JButton viderButton;
    private JButton previousButton;
    private JButton nextButton;
    private JButton achatButton;
    private JLabel passwordLabel;
    private JLabel loginLabel;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField stockField;
    private JTextField quantityField;
    private JTextField totalLabel;
    private JTextField BIENVENUESURLEMARAICHERTextField;

    private DefaultTableModel model;

    public GUIMaraicherEnLigne() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel1);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        loginButton.setActionCommand("Login");
        logoutButton.setActionCommand("Logout");
        achatButton.setActionCommand("Achat");
        validerButton.setActionCommand("AchatTot");
        supprimerButton.setActionCommand("Supprimer");
        viderButton.setActionCommand("Vider");
        previousButton.setActionCommand("PrevArticle");
        nextButton.setActionCommand("NextArticle");
        logoutButton.setEnabled(false);
        achatButton.setEnabled(false);
        validerButton.setEnabled(false);
        supprimerButton.setEnabled(false);
        viderButton.setEnabled(false);
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);

    }
    public void setControleur(ControleurAchat c) {
        loginButton.addActionListener(c);
        logoutButton.addActionListener(c);
        achatButton.addActionListener(c);
        validerButton.addActionListener(c);
        supprimerButton.addActionListener(c);
        viderButton.addActionListener(c);
        previousButton.addActionListener(c);
        nextButton.addActionListener(c);

        this.addWindowListener(c);
    }
    public String getLogin() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }
    public boolean getCheckBox() {
        return isnewCheckBox.isSelected();
    }
    public void setStatusError(String errorMessage) {
        BIENVENUESURLEMARAICHERTextField.setForeground(Color.RED);
        BIENVENUESURLEMARAICHERTextField.setText(errorMessage);
    }
    public void setStatusSuccess(String successMessage) {
        BIENVENUESURLEMARAICHERTextField.setForeground(Color.GREEN);
        BIENVENUESURLEMARAICHERTextField.setText(successMessage);
    }

}
