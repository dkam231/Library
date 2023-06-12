package finalProj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Library library;
    
    public ClientHandler(Socket socket, Library library) {
        this.clientSocket = socket;
        this.library = library;
        
        try {
            // Set up input and output streams for the socket
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void sendMessage(String message) {
        // Send a message to the client
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            System.err.println("Error sending message to client");
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        try {
            // Read incoming requests from the client and process them
            while (true) {
                String request;
                try {
                    request = (String) inputStream.readObject();
                if (request.startsWith("login")) {
                    // Parse the username and password from the request and attempt to log in the user
                    String[] parts = request.split(",");
                    String username = parts[1];
                    String password = parts[2];
                    boolean isLibrarian = Boolean.parseBoolean(parts[3]);
                    library.login(username, password,isLibrarian);
                    sendMessage("sucess");
                } else if (request.startsWith("register")) {
                    // Parse the username, password, and librarian status from the request and register the user
                    String[] parts = request.split(",");
                    String username = parts[1];
                    String password = parts[2];
                    boolean isLibrarian = Boolean.parseBoolean(parts[3]);
                    library.register(username, password, isLibrarian);
                    sendMessage("sucess");
                } else if (request.startsWith("checkout")) {
                    // Parse the book ID from the request and attempt to check out the book
                    String[] parts = request.split(",");
                    int bookId = Integer.parseInt(parts[1]);
                    
                    //TODO: call the checkOutBook method of the Library class and send a response back to the client
                } else if (request.startsWith("return")) {
                    // Parse the book ID from the request and attempt to return the book
                    String[] parts = request.split(",");
                    int bookId = Integer.parseInt(parts[1]);
                    //TODO: call the returnBook method of the Library class and send a response back to the client
                } else if (request.startsWith("extend")) {
                    // Parse the book ID from the request and attempt to extend the due date
                    String[] parts = request.split(",");
                    int bookId = Integer.parseInt(parts[1]);
                    //TODO: call the extendDueDate method of the Library class and send a response back to the client
                } else if (request.startsWith("getbooks")) {
                    // Return a list of all books in the library
                    List<Book> books = library.getBooks();
                    StringBuilder response = new StringBuilder();
                    for (Book book : books) {
                        response.append(book.toString());
                        response.append("\n");
                    }
                    sendMessage(response.toString());
                } else {
                   sendMessage("Invalid request.");
                }
            }
         catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
         
    }
    }   catch (Exception e) {
    System.out.println("Error: " + e.getMessage());
     }
    }
}
