package WebServer.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class HandlerImage implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        String requestPath = exchange.getRequestURI().getPath();

        System.out.println("Nouvelle requete de type Image");
        if (requestPath.endsWith(".jpg"))
        {

            String fichier = "./" + requestPath;
            File file = new File(fichier);

            System.out.println(file.getAbsolutePath());
            if (file.exists())
            {
                exchange.sendResponseHeaders(200, file.length());
                exchange.getResponseHeaders().set("Content-Type","image/jpeg");

                OutputStream os = exchange.getResponseBody();
                Files.copy(file.toPath(), os);
                os.close();
            }
            else
                Erreur404(exchange);
        }
        else
            Erreur404(exchange);
    }

    private void Erreur404(HttpExchange exchange) throws IOException
    {
        String reponse = "Could not find the targeted image...";
        exchange.sendResponseHeaders(404, reponse.length());
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        OutputStream os = exchange.getResponseBody();
        os.write(reponse.getBytes());
        os.close();
        System.out.println("KO");
    }
}

