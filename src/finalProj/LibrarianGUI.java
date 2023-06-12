package finalProj;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LibrarianGUI extends Application {

    @FXML
    private ListView<Book> booksList;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField bookAuthorField;

    @FXML
    private TextField bookTitleField;

    @FXML
    private TextField bookISBN;
    @FXML
    private TextField passwordField;

    @FXML
    private TextField usernameField;
    @FXML
    private Button logoutButton;

    @FXML
    private ToggleButton isLibrarian;


    private Library library;
    private User currentUser;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file
        List<String> styles = Arrays.asList("yellow", "white", "green", "red","teal","Light blue","cyan");
        int randomIndex = new Random().nextInt(styles.size());
        String randomStyle = styles.get(randomIndex);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("librarian.fxml"));
        loader.setController(new LibrarianGUI(currentUser,library));
        GridPane root = loader.load();
        root.setStyle("-fx-background-color: "+randomStyle);

        // Initialize the Library and set the books list
        library = new Library();
        List<Book> books = library.getBooks();
        booksList.getItems().addAll(books);

      

        // Show the scene
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Library Management System");
        primaryStage.show();
    }
    
    public LibrarianGUI(User user,Library library){
        currentUser=user;
        booksList= new ListView<>();
         this.library=library;
    }

    @FXML
    void handleAddBook(ActionEvent event) {
        // Get the book information from the fields
        String title = bookTitleField.getText();
        String author = bookTitleField.getText();
        String isbn=bookISBN.getText();

        if (!title.isEmpty() && !author.isEmpty() && !isbn.isEmpty()) {
            // Add the book to the library
            library.addBook(new Book(title,author,isbn,false,null,"[null]",0));

            // Clear the fields and refresh the books list
            bookTitleField.clear();
            bookTitleField.clear();
            bookISBN.clear();
        } else {
            errorLabel.setText("Please enter a title and author.");
        }
    }
    
    @FXML
    void handleAddUser(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill out all fields");
            return;
        }

        

        if (library.addUser(username, User.encrypString(password),false)) {
            errorLabel.setText("User added successfully");
        } else {
            errorLabel.setText("Error adding user");
        }
    }
    @FXML
    void handleLogout(ActionEvent event) {
        DataFileManager.saveBooks(library.getBooks());
        DataFileManager.saveUsers(library.getUsers());
        currentUser = null;
        Platform.exit();
    }

    @FXML
    void handleRemoveBook(ActionEvent event) {
        String title = bookTitleField.getText();
        String author = bookTitleField.getText();
        String isbn = bookISBN.getText();

        if (isbn.isEmpty()) {
            errorLabel.setText("Please enter ISBN");
            return;
        }


        if (library.removeBook(title,author,isbn)) {
            errorLabel.setText("Book removed successfully");
        } else {
            errorLabel.setText("Error removing book");
        }
    }

    @FXML
    void handleRemoveUser(ActionEvent event) {
        String username = usernameField.getText();

        if (username.isEmpty()) {
            errorLabel.setText("Please enter username");
            return;
        }


        if (library.removeUser(username)) {
            errorLabel.setText("User removed successfully");
        } else {
            errorLabel.setText("Error removing user");
        }
    }

}

