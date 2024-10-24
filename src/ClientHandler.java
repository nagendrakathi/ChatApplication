import java.io.*;
import java.net.*;
class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter writer;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            writer = new PrintWriter(socket.getOutputStream(), true);
            printToClient("Enter your username: ");
            username = reader.readLine();
            while (!ChatServer.addUser(username)) {
                printToClient("Username is taken. Try another one: ");
                username = reader.readLine();
            }
            printToClient("Welcome " + username + "! You can start messaging.");
            ChatServer.broadcast(username + " has joined the chat!", this);

            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                String serverMessage = "[" + username + "]: " + clientMessage;
                System.out.println(serverMessage);
                ChatServer.broadcast(serverMessage, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ChatServer.removeUser(username, this);
            ChatServer.broadcast(username + " has left the chat.", this);
        }
    }
    public void sendMessage(String message) {
        writer.println(message);
    }
    private void printToClient(String message) {
        writer.println(message);
    }
    
}
