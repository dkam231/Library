package finalProj;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

public class Client extends Application {

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private User user;
    
    private Scene loginScene;

    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        // load FXML files for login, librarian, and patron screens
        List<String> styles = Arrays.asList("yellow", "white", "green", "red","teal","Light blue","cyan");
        int randomIndex = new Random().nextInt(styles.size());
        String randomStyle = styles.get(randomIndex);

        connectToServer();
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("LoginScreen.fxml"));
         LoginController controller = new LoginController();
         controller.setClient(this);
         loginLoader.setController(controller);
         GridPane pane= loginLoader.load();
         pane.setStyle("-fx-background-color: "+randomStyle);
        loginScene = new Scene(pane);
        
        // set login screen as initial scene
        primaryStage.setScene(loginScene);
        primaryStage.show();
        System.out.println("STYLESHEET_CASPIAN");
        
    }
    

    
    @Override
    public void stop() throws Exception {
        // close socket before shutting down client
        socket.close();
    }
    
    public void connectToServer() throws IOException {
        // connect to server socket and initialize input and output streams
        socket = new Socket("localhost", 8080);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
    }
    
    public User login(String username, String password) throws IOException, ClassNotFoundException {
        // send login credentials to server
        output.writeObject("login," + username + "," + password);
        output.flush();
        
        // read server response
        Object response = input.readObject();
        
        if (response instanceof User) {
            // save logged-in user and return user object
            user = (User) response;
            return user;
        } else {
            // return null if login failed
            return null;
        }
    }
    
    public List<Book> getAvailableBooks() throws IOException, ClassNotFoundException {
        // send getAvailableBooks request to server
        output.writeObject("getAvailableBooks");
        output.flush();
        
        // read server response
        Object response = input.readObject();
        
        if (response instanceof List<?>) {
            // return list of available books
            return (List<Book>) response;
        } else {
            // return null if request failed
            return null;
        }
    }
    
    public String checkoutBook(String isbn) throws IOException, ClassNotFoundException {
        // send checkoutBook request to server
        output.writeObject("checkout," + user.getUsername() + "," + isbn);
        output.flush();
        
        // read server response
        Object response = input.readObject();
        
        if (response instanceof String) {
            // return response message
            return (String) response;
        } else {
            // return null if request failed
            return null;
        }
    }
    
    public String returnBook(String isbn) throws IOException, ClassNotFoundException {
        // send returnBook request to server
        output.writeObject("return," + user.getUsername() + "," + isbn);
        output.flush();
        
        // read server response
        Object response = input.readObject();
        
        if (response instanceof String) {
            // return response message
            return (String) response;
        } else {
            // return null if request failed
            return null;
        }
    }
    
    public String extendBook(String isbn) throws IOException, ClassNotFoundException {
        // send extendBook request to server
        output        .writeObject("extend," + user.getUsername() + "," + isbn);
        output.flush();
        
        // read server response
        Object response = input.readObject();
        
        if (response instanceof String) {
            // return response message
            return (String) response;
        } else {
            // return null if request failed
            return null;
        }
    }
    
    public List<Book> getCheckedOutBooks() throws IOException, ClassNotFoundException {
        // send getCheckedOutBooks request to server
        output.writeObject("getCheckedOutBooks," + user.getUsername());
        output.flush();
        
        // read server response
        Object response = input.readObject();
        
        if (response instanceof List<?>) {
            // return list of checked out books
            return (List<Book>) response;
        } else {
            // return null if request failed
            return null;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Hi");
        Application.launch(args);
    }
}

