package WebServer.Handlers;

import BDaccess.VESPAP_BD;
import Classe_Metier.Articles;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ArticlesHandler implements HttpHandler {

    private static ArrayList<Articles> listeArticle;
    private static VESPAP_BD vespapBd;

    public ArticlesHandler(VESPAP_BD bd) throws SQLException {
        this.vespapBd = bd;
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
         try {

             Articles articles = ConvertJsonToArticle(readRequestBody(exchange));
            if (articles.getIntitule().isEmpty() || articles.getImage().isEmpty())
            {
                sendResponse(exchange, 200, "c vide cousin");
            }
            else {
                if (articles.getPrix() >= 0.0 && articles.getId() <= vespapBd.getArticlesCount() && articles.getId()>0) {
                    vespapBd.Change_Article(articles.getId(), articles.getPrix(), articles.getStock());
                    updateArticles(articles.getId(), articles);
                    sendResponse(exchange, 200, "Article a jour dans la BD");
                } else
                    sendResponse(exchange, 200, "Ta cru cetait noel");
            }
         }
         catch (JsonProcessingException e)
         {
             sendResponse(exchange, 400, "Json mal forme");
         } catch (SQLException e) {
             System.out.println(e.toString());
             sendResponse(exchange, 400, "BD error");
         }

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
            json.append(", \"stock\": \"").append(listeArticle.get(i).getStock()).append("\"");
            json.append(", \"image\": \"").append(listeArticle.get(i).getId()).append("\"");
            json.append("}");
            if(i < listeArticle.size() -1)
                json.append(",");
        }
        json.append("]");
        return json.toString();
    }
    private static Articles ConvertJsonToArticle(String json) throws JsonProcessingException {

        ObjectMapper objectmapper = new ObjectMapper();
        Articles articles = objectmapper.readValue(json, Articles.class);
        return articles;
    }
    private static String readRequestBody(HttpExchange exchange) throws IOException
    {
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
            requestBody.append(line);
        }
        reader.close();
        return requestBody.toString();
    }

    private static void updateArticles(int articlesID, Articles updateArticle)
    {
        if(articlesID>=1 && articlesID<=listeArticle.size())
        {
            listeArticle.set(articlesID-1, updateArticle);
        }
    }
}
