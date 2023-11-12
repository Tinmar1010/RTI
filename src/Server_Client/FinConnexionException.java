package Server_Client;

public class FinConnexionException extends Exception
{
    private Reponse reponse;
    public FinConnexionException(Reponse reponse)
    {
        super("Fin de Connexion décidée par protocole");
        this.reponse = reponse;
    }
    public Reponse getReponse()
    {
        return reponse;
    }
}
