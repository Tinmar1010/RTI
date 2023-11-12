package Test_Class;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TestServerConnection {
    public static void main(String[] args) {
        try {
            // Se connecte au serveur local sur le port 8080
            Socket socket = new Socket("192.168.228.167",50000);

            // Crée un lecteur pour lire les données du serveur
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Envoie un message au serveur
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("Hello, Server!");

            // Lit la réponse du serveur
            String serverResponse = reader.readLine();
            System.out.println("Server says: " + serverResponse);

            // Ferme les flux et le socket
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
