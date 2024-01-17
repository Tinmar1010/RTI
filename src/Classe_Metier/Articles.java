package Classe_Metier;


import java.io.Serializable;

public class Articles implements Serializable {

    private String intitule;
    private int id;
    private int stock;
    private float prix;
    private String image;
    public Articles()
    {
        setId(0);
        setImage("Carottes.jpg");
        setPrix(1);
        setStock(0);
        setIntitule("");
    }
    public Articles(int id, int stock, float prix, String images)
    {
        setStock(stock);
        setId(id);
        setImage(images);
        setPrix(prix);
    }
    public Articles(int id, String intitule, float prix, int stock, String image) {
        setId(id);
        setIntitule(intitule);
        setPrix(prix);
        setStock(stock);
        setImage(image);
    }
    public Articles(String inti, float prix, int stock) {
        setIntitule(inti);
        setPrix(prix);
        setStock(stock);
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

    public void setStock(int stock)
    {
        if(stock >=0)
            this.stock = stock;
    }
    public void setIntitule(String inti)
    {
        intitule = inti;
    }

    public int getId()
    {
        return this.id;
    }
    public int getStock()
    {
        return this.stock;
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
