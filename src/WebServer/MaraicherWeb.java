package WebServer;

import BDaccess.BeanAccesBD;
import BDaccess.VESPAP_BD;
import WebServer.Handlers.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class MaraicherWeb {
    private final HttpServer server;
    private final VESPAP_BD bd;

    public MaraicherWeb() throws IOException, SQLException, ClassNotFoundException {
        /* Instantiation du bean de la BD */
        bd = new VESPAP_BD("MySql", "localhost", "PourStudent", "Student", "PassStudent1_");

        /* Creation du server */
        server = HttpServer.create(new InetSocketAddress(8080),0);

        /* Creation des contexts */
        server.createContext("/", new rootHandler());
        server.createContext("/css", new HandlerCss());
        server.createContext("/api/articles", new ArticlesHandler(bd));
        server.createContext("/js", new HandlerJs());
        server.createContext("/images", new HandlerImage());
        server.start();
    }
}
