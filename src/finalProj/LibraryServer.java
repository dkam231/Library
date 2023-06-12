package finalProj;import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class LibraryServer extends Application {

    private List<User> users;
    private List<Book> books;
    
    private ServerSocket serverSocket;
    
    private Scene loginScene;
    private Scene librarianScene;
    private Scene patronScene;
    private ArrayList<ClientHandler> clientHandlers;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // load user and book data (or create new data if none exists)
        clientHandlers= new ArrayList<>();
        users = new ArrayList<>();
        try {
            File audioFile = new File("Music/soft-rain-ambient-111154.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error playing audio: " + ex.getMessage());
        }
       // Library library= new Library();
        // start server socket and accept client connections
        serverSocket = new ServerSocket(8080);
        System.out.println("Server started on port " + 8080);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("15 minutes have passed. Stopping server...");
                try {
                    stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        }, 15 * 60 * 1000); // 15 minutes in milliseconds
        
        
        while (true) {
            Socket socket = serverSocket.accept();
            
            // create new thread to handle client requests
            ClientHandler handler = new ClientHandler(socket, new Library(books, users));
            clientHandlers.add(handler);
            new Thread(handler).start();

            
        }
    }
    
    @Override
    public void stop() throws Exception {
        // save user and book data before shutting down server
        
        serverSocket.close();
    }
    public void broadcastMessage(String message) {
        // Send a message to all connected clients
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendMessage(message);
        }
    }
    public void removeClientHandler(ClientHandler clientHandler) {
        // Remove a client handler when a client disconnects
        clientHandlers.remove(clientHandler);
    }
    

    public static void main(String[] args) {
        Application.launch(args);
    }

}

