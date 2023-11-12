package Classe_Metier;


public class Articles {

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
    }
    public Articles(int id, int quantite, float prix, String images)
    {
        setQuantite(quantite);
        setId(id);
        setImage(images);
        setPrix(prix);
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
}
