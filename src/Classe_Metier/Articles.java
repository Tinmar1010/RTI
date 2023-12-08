package Classe_Metier;


import java.io.Serializable;

public class Articles implements Serializable {

    private String intitule;
    private int id;
    private int quantite;
    private float prix;
    private String image;
    public Articles()
    {
        setId(0);
        setImage("Carottes.jpg");
        setPrix(1);
        setQuantite(0);
        setIntitule("");
    }
    public Articles(int id, int quantite, float prix, String images)
    {
        setQuantite(quantite);
        setId(id);
        setImage(images);
        setPrix(prix);
    }
    public Articles(int id, String intitule, float prix, int quantite, String image) {
        setId(id);
        setIntitule(intitule);
        setPrix(prix);
        setQuantite(quantite);
        setImage(image);
    }
    public Articles(String inti, float prix, int quantite) {
        setIntitule(inti);
        setPrix(prix);
        setQuantite(quantite);
    }

    public void setId(int id)
    {
        if (id >=0)
            this.id = id;
    }

    public void setImage(String image)
    {
        if(image !="")
            this.image = image;
    }

    public void setPrix(float prix)
    {
        if(prix !=0)
            this.prix = prix;
    }

    public void setQuantite(int quantite)
    {
        if(quantite>=0)
            this.quantite = quantite;
    }
    public void setIntitule(String inti)
    {
        intitule = inti;
    }

    public int getId()
    {
        return this.id;
    }
    public int getQuantite()
    {
        return this.quantite;
    }
    public float getPrix()
    {
        return this.prix;
    }
    public String getImage()
    {
        return this.image;
    }
    public String getIntitule() {return this.intitule;}
}
