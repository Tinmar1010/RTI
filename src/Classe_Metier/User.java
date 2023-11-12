package Classe_Metier;

public class User
{
    private String login;
    private String mdp;

    public User()
    {
        setLogin("");
        setMdp("");
    }
    public User(String log, String motdepasse)
    {
        setLogin(log);
        setMdp(motdepasse);
    }

    public void setLogin(String log)
    {
        login = log;
    }
    public void setMdp(String motdepasse)
    {
        mdp = motdepasse;
    }

    public String getLogin()
    {
        return login;
    }
    public String getMdp()
    {
        return mdp;
    }

}
