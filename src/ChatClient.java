import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private String username;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public ChatClient(String serverAddress, int port) {
        try {
            // Initialize the socket
            socket = new Socket(serverAddress, port);

            // Initialize the writer and reader
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Connected to the chat server");
        } catch (IOException e) {
            System.out.println("Error connecting to the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void execute() {
        if (socket != null && writer != null && reader != null) {
            new ReadThread().start();  // Start thread for reading messages
            new WriteThread().start(); // Start thread for sending messages
        } else {
            System.out.println("Connection was not established. Please check the server.");
        }
    }

    // Thread for reading messages from the server
    class ReadThread extends Thread {
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Error in reading thread: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Thread for sending messages to the server
    class WriteThread extends Thread {
        Scanner scanner = new Scanner(System.in);

        public void run() {
            try {
                System.out.print("Enter your username: ");
                username = scanner.nextLine();
                writer.println(username);  // Send username to the server

                String text;
                do {
                    text = scanner.nextLine();
                    writer.println(text);  // Send message to the server
                } while (!text.equalsIgnoreCase("bye"));

                // Close the socket when done
                socket.close();
            } catch (IOException e) {
                System.out.println("Error in writing thread: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // The main method to start the ChatClient
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java ChatClient <server address> <port number>");
            System.exit(0);
        }

        String serverAddress = args[0];
        int port = Integer.parseInt(args[1]);

        ChatClient client = new ChatClient(serverAddress, port);
        client.execute();
    }
}
