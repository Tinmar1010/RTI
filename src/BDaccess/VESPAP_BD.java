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
    private int create_new_user(String login, String password) throws SQLException, ClassNotFoundException {

        ResultSet result =  check_user(login);

        if(result == null) {
            //DB Problem or user doesnt exist
            return -1;
        }
        else
        {
            int rep = bdacces.executeUpdate("INSERT INTO clients VALUES (0, \""+login+"\", \""+password+"\");");
            if (rep == -1)
            {
                //DB error
                return rep;
            }
            else //Succes
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

    public ArrayList<Factures> Get_Factures(int idclient, BeanAccesBD BAD) throws SQLException
    {
        ArrayList<Factures> listefactures = new ArrayList<Factures>();

        /*int ret = super.getTuple("select * from factures where idlcient = "+ idclient + ";", BAD);
        if(ret == -1)//DB error
            return null;
        else
        {
            while(BAD.getResult().next())
            {
                Factures fact = new Factures();
                fact.setIdFactures(BAD.getResult().getInt(0));
                fact.setDate(BAD.getResult().getDate(1));
                fact.setMontant(BAD.getResult().getFloat(2));
                fact.setPaye(BAD.getResult().getBoolean(3));

                listefactures.add(fact);
            }
            return listefactures;
        }*/
        return listefactures;
    }

    public int Pay_Facture(int idFacture, String nom, String numVisa, BeanAccesBD BAD)
    {
        /*int ret = super.getTuple("select * from clients where nom = " +"'" +  nom + "'" + "AND numvisa = " +"'" + numVisa + "';", BAD);

        if(ret == -1) {
            //DB Problem
            return ret;
        }
        else
        {
            if (BAD.getResult() == null) {
                //User doesnt exist
                return 0;
            }
            else
                return 1;
        }

         */
        return 0;
    }
  


}
