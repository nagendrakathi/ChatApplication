import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static Set<String> usernames = new HashSet<>();  // Store logged-in usernames
    private static Set<ClientHandler> clientHandlers = new HashSet<>();  // Store active clients
    
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server is listening on port 1234...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();  // Start a new thread for each client
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcasts messages to all clients
    public static void broadcast(String message, ClientHandler excludeUser) {
        for (ClientHandler client : clientHandlers) {
            if (client != excludeUser) {
                client.sendMessage(message);
            }
        }
    }

    // Removes a client from the list
    public static void removeUser(String username, ClientHandler clientHandler) {
        boolean removed = usernames.remove(username);
        if (removed) {
            clientHandlers.remove(clientHandler);
            System.out.println(username + " has left the chat.");
        }
    }

    // Check if the username is already in use
    public static boolean addUser(String username) {
        return usernames.add(username);
    }
}
