package WebServer.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class rootHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("Nouvelle requete vers index.html recue !");
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();

        if (requestMethod.equalsIgnoreCase("GET"))
        {
            /* if .html provided, try to open it and send it */
            if (requestPath.endsWith(".html"))
            {
                File file = new File("src/WebServer/"+requestPath);
                if (file.exists())
                {
                    exchange.sendResponseHeaders(200, file.length());
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    OutputStream os = exchange.getResponseBody();
                    Files.copy(file.toPath(), os);
                    os.close();
                    System.out.println("OK");
                }
                else
                    Erreur404(exchange);

            }
            /* If not, show the default page */
            else
            {
                File file = new File("src/WebServer/index.html");
                if (file.exists())
                {
                    exchange.sendResponseHeaders(200, file.length());
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    OutputStream os = exchange.getResponseBody();
                    Files.copy(file.toPath(), os);
                    os.close();
                    System.out.println("OK");
                }
                else
                    Erreur404(exchange);

            }


        }
        else
            Erreur404(exchange);

    }
    private void Erreur404(HttpExchange exchange) throws IOException
    {
        String reponse = "HTML File not found on the server";
        exchange.sendResponseHeaders(404, reponse.length());
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        OutputStream os = exchange.getResponseBody();
        os.write(reponse.getBytes());
        os.close();
        System.out.println("KO");
    }
}
