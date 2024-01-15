package WebServer.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class HandlerCss implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("Nouvelle requete de type CSS");
        String requestPath = exchange.getRequestURI().getPath();

        if (requestPath.endsWith(".css"))
        {
            String fichier = "src/WebServer/" + requestPath;
            File file = new File(fichier);
            if (file.exists())
            {
                exchange.sendResponseHeaders(200, file.length());
                exchange.getResponseHeaders().set("Content-Type", "text/css");
                OutputStream os = exchange.getResponseBody();
                Files.copy(file.toPath(), os);
                os.close();
                System.out.println("OK");
            }
            else Erreur404(exchange);
        }
        else Erreur404(exchange);
    }
    private void Erreur404(HttpExchange exchange) throws IOException
    {
        String reponse = "Could not find the targeted CSS file";
        exchange.sendResponseHeaders(404, reponse.length());
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        OutputStream os = exchange.getResponseBody();
        os.write(reponse.getBytes());
        os.close();
        System.out.println("KO");
    }
}