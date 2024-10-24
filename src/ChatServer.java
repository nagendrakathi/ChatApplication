import java.io.*;
import java.net.*;
import java.util.*;
public class ChatServer {
    private static Set<String> usernames = new HashSet<>();
    private static Set<ClientHandler> clientHandlers = new HashSet<>();
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server is listening on port 1234...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void broadcast(String message, ClientHandler excludeUser) {
        for (ClientHandler client : clientHandlers) {
            if (client != excludeUser) {
                client.sendMessage(message);
            }
        }
    }
    public static void removeUser(String username, ClientHandler clientHandler) {
        boolean removed = usernames.remove(username);
        if (removed) {
            clientHandlers.remove(clientHandler);
            System.out.println(username + " has left the chat.");
        }
    }
    public static boolean addUser(String username) {
        return usernames.add(username);
    }
}