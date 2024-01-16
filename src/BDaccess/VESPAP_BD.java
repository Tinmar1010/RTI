package BDaccess;

import Classe_Metier.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class VESPAP_BD {

    public BeanAccesBD bdacces;

    public VESPAP_BD(String type, String server, String dbName, String user, String password) throws ClassNotFoundException, SQLException {
        bdacces = new BeanAccesBD(type, server, dbName, user, password);
    }

    public int Login(String login, String password,  boolean isnew) throws SQLException, ClassNotFoundException {
        if(isnew)
        {
            int ret = create_new_user(login, password);
            return ret;
        }
        else
        {
            int ret = check_user_password(login, password);
            return ret;
        }
    }
    public String get_the_password(String login)throws SQLException, ClassNotFoundException {

        ResultSet result = bdacces.executeQuery("SELECT * FROM clients where login = " + "\"" +login + "\";");
        result.next();
        String mdp = result.getString("password");
        System.out.println(mdp);
        return mdp;

    }
    private int create_new_user(String login, String password) throws SQLException, ClassNotFoundException {

        ResultSet result =  check_user(login);

        if(result == null) {
            //DB Problem or user doesnt exist
            return -1;
        }
        else
        {
            System.out.println("Login = " + login);
            int rep = bdacces.executeUpdate("INSERT INTO clients VALUES (0, \""+login+"\", \""+password+"\");");

            return rep;
        }
    }
    private int check_user_password(String login, String password) throws SQLException, ClassNotFoundException {

        ResultSet result =  check_user(login);
        if(result == null) {
            //DB Problem or user doesnt exist
            System.out.println("DB ERROR");
            return -1;
        }
        else
        {
            String mdp = result.getString("password");
            if(mdp.equals(password))
            {
                System.out.println("Success !!!!");
                //Success
                return 1;
            }
            else
            {
                System.out.println("Bad password");
                System.out.println(password);
                System.out.println(mdp);
                //Bad password
                return -2;
            }
        }
    }
    private ResultSet check_user(String login) throws SQLException, ClassNotFoundException {

        String requete = "SELECT * FROM clients where login = " + "\"" +login + "\";";
        ResultSet result = bdacces.executeQuery(requete);
        result.next();
        return result;
    }

    public ArrayList<Factures> Get_Factures(int idclient) throws SQLException
    {
        ArrayList<Factures> listefactures = new ArrayList<Factures>();

        ResultSet ret = bdacces.executeQuery("select * from factures where idClient = "+ "\"" +idclient + "\";");

        while(ret.next())
        {
            Factures fact = new Factures();
            fact.setIdFactures(ret.getInt("id"));
            fact.setIdClient(ret.getInt("idClient"));
            fact.setDate(ret.getDate("date"));
            fact.setMontant(ret.getFloat("montant"));
            fact.setPaye(ret.getBoolean("paye"));

            listefactures.add(fact);
        }
        return listefactures;

    }


    public ArrayList<Articles> Get_Articles() throws SQLException
    {
        ArrayList<Articles> listeArticles = new ArrayList<Articles>();

        ResultSet ret = bdacces.executeQuery("select * from articles ;");

        while(ret.next())
        {
            Articles art = new Articles();
            art.setId(ret.getInt("id"));
            art.setIntitule(ret.getString("intitule"));
            art.setPrix(ret.getFloat("prix"));
            art.setQuantite(ret.getInt("stock"));
            art.setImage(ret.getString("image"));

            listeArticles.add(art);
        }
        return listeArticles;

    }
    public int Pay_Facture(int idFactures) throws SQLException {
        int rep = 0;
        try {
            rep = bdacces.executeUpdate("UPDATE factures SET paye = 1 WHERE id = " +idFactures + ";");
            return rep;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int Change_price(int idArticle, float prixArticle)throws SQLException
    {
        int rep = 0;
        rep = bdacces.executeUpdate("UPDATE factures SET prix = "+prixArticle+" WHERE id = " +idArticle + ";");
        return rep;
    }
    public int Change_Qantity(int idArticle, float quantiteArticle)throws SQLException
    {
        int rep = 0;
        rep = bdacces.executeUpdate("UPDATE factures SET prix = "+quantiteArticle+" WHERE id = " +idArticle + ";");
        return rep;
    }
}
