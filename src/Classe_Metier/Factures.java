package Classe_Metier;

import java.io.Serializable;
import java.util.*;

public class Factures implements Serializable {

    static int idFactures;
    private Date date;
    private float montant;
    private boolean paye;
    public int idClient;


    public Factures()
    {
        setIdFactures(0);
        setDate(new Date());
        setMontant(0);
        setPaye(false);
    }
    public int getIdFactures() {
        return idFactures;
    }

    public boolean isPaye() {
        return paye;
    }

    public Date getDate()
    {
        return date;
    }

    public float getMontant()
    {
        return montant;
    }
    public int getIdClient(){return idClient;}

    public void setIdFactures(int idFactures)
    {
        this.idFactures = idFactures;
    }

    public void setMontant(float montant)
    {
        this.montant = montant;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public void setPaye(boolean paye)
    {
        this.paye = paye;
    }
    public void setIdClient(int idC)
    {
        idClient = idC;
    }
}
