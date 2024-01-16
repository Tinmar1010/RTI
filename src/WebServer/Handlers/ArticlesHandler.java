package WebServer.Handlers;

import BDaccess.VESPAP_BD;
import Classe_Metier.Articles;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class ArticlesHandler implements HttpHandler {

    private static ArrayList<Articles> listeArticle;

    public ArticlesHandler(VESPAP_BD bd) throws SQLException {
        listeArticle = bd.Get_Articles();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        String requestMethod = exchange.getRequestMethod();

        if (requestMethod.equalsIgnoreCase("GET"))
        {
            System.out.println("Nouvelle requete de type GET sur Articles");
            String reponseArticles = ConvertArticleToJSON();
            sendResponse(exchange, 200, reponseArticles);

        }
        if(requestMethod.equalsIgnoreCase("PUT"))
        {
            System.out.println("Nouvelle requete de type PUT sur Articles");

        }
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private static String ConvertArticleToJSON()
    {
        StringBuilder json = new StringBuilder("[");
        for (int i=0; i<listeArticle.size();i++)
        {
            json.append("{\"id\": \"").append(listeArticle.get(i).getId()).append("\"");
            json.append(", \"intitule\": \"").append(listeArticle.get(i).getIntitule()).append("\"");
            json.append(", \"prix\": \"").append(listeArticle.get(i).getPrix()).append("\"");
            json.append(", \"stock\": \"").append(listeArticle.get(i).getQuantite()).append("\"");
            json.append(", \"image\": \"").append(listeArticle.get(i).getId()).append("\"");
            json.append("}");
            if(i < listeArticle.size() -1)
                json.append(",");
        }
        json.append("]");
        return json.toString();
    }
    private static void updateArticles(int articlesID, Articles updateArticle)
    {
        if(articlesID>=1 && articlesID<=listeArticle.size())
        {
            listeArticle.set(articlesID-1, updateArticle);
        }
    }
}
