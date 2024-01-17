import Classe_Metier.Articles;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
    private JLabel Image;

    private DefaultTableModel model;

    String[] Column = {"ID","Article", "Prix à l'unité", "Quantité", "Image"};

    public GUIMaraicherEnLigne() {

        model = new DefaultTableModel(null, Column);
        table1.setModel(model);
        table1.removeColumn(table1.getColumnModel().getColumn(0)); // Remove ID column
        table1.removeColumn(table1.getColumnModel().getColumn(3)); // Remove Image column
        setContentPane(panel1);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel1);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        loginButton.setActionCommand("Login");
        logoutButton.setActionCommand("Logout");
        achatButton.setActionCommand("Achat");
        validerButton.setActionCommand("CONFIRM");
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
        quantityField.setEnabled(false);

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
    public void disableLoginButton() {
        loginButton.setEnabled(false);
    }
    public void enableLoginButton() {
        loginButton.setEnabled(true);
    }
    public void disableLogoutButton() {
        logoutButton.setEnabled(false);
    }
    public void enableLogoutButton() {
        logoutButton.setEnabled(true);
    }
    public void setNameField(String value) {
        nameField.setText(value);
    }
    public void setPriceField(String price) {
        priceField.setText(price);
    }
    public void setStockField(String stock) {
        stockField.setText(stock);
    }

    public String getQuantityField() {
        return quantityField.getText();
    }
    public int getStockField() {
        return Integer.parseInt(stockField.getText());
    }
    public void setImage(String imagePath) {
        StringBuilder _sb = new StringBuilder(imagePath);
        _sb.insert(0, "images/");

        ImageIcon icon = new ImageIcon(_sb.toString());
        Image.setIcon(icon);
    }

    public void setGuiLogin() {
        logoutButton.setEnabled(true);
        achatButton.setEnabled(true);
        validerButton.setEnabled(true);
        supprimerButton.setEnabled(true);
        viderButton.setEnabled(true);
        previousButton.setEnabled(true);
        nextButton.setEnabled(true);
        loginButton.setEnabled(false);
        quantityField.setEnabled(true);
    }
    public void setGuiLogout() {
        loginButton.setEnabled(true);
        logoutButton.setEnabled(false);
        achatButton.setEnabled(false);
        validerButton.setEnabled(false);
        supprimerButton.setEnabled(false);
        viderButton.setEnabled(false);
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);
        quantityField.setEnabled(false);
    }

    public void clearArticle() {
        setNameField("");
        setImage("");
        setPriceField("");
        setStockField("");
    }
    public void addArticle(Articles art) {
        model.addRow(new Object[] {art.getId(), art.getIntitule(), art.getPrix(), art.getStock(), art.getImage()});
    }
    public void setArticle(Articles art) {
        setNameField(art.getIntitule());
        setImage(art.getImage());
        setPriceField(String.valueOf(art.getPrix()));
        setStockField(String.valueOf(art.getStock()));
    }
    public void clearEntries() {
        model.setRowCount(0);
    }

    public Articles getSelectedArticle() {
        int value = table1.getSelectedRow();
        if (value == -1)
            return null;
        else {

            Articles art = new Articles((Integer)model.getValueAt(value, 0), (String)model.getValueAt(value, 1), (Float)model.getValueAt(value, 2), (Integer)model.getValueAt(value, 3), (String)model.getValueAt(value, 4));
            return art;
        }

    }

    public List<Articles> getAllArticles() {
        List<Articles> articlesList = new ArrayList<Articles>();
        for (int i = 0; i < model.getRowCount(); i++) {
            Articles art = new Articles((Integer)model.getValueAt(i, 0), (String)model.getValueAt(i, 1), (Float)model.getValueAt(i, 2), (Integer)model.getValueAt(i, 3), (String)model.getValueAt(i, 4));
            articlesList.add(art);
        }
        return articlesList;
    }

    public int isBucketEmpty() {
        return table1.getRowCount();
    }
    public int isArticleSelected() {
        return table1.getSelectedRow();
    }

}
