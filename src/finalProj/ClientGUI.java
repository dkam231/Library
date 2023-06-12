package finalProj;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ClientGUI extends Application {

    private Library library;
    private User currentUser;

    @FXML
    private ListView<Book> availableBooksList;


    @FXML
    private ListView<Book> myBooksList;

    @FXML
    private TextField returnField;

    @FXML
    private TextField searchField;

    @FXML
    private Button logoutButton;

    @FXML
    private Label errorLabel;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        List<String> styles = Arrays.asList("yellow", "white", "green", "red","teal","Light blue","cyan");
        int randomIndex = new Random().nextInt(styles.size());
        String randomStyle = styles.get(randomIndex);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientGUI.fxml"));
        loader.setController(new ClientGUI(currentUser,library));
        GridPane root = loader.load();
        root.setStyle("-fx-background-color: "+randomStyle);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Library Checkout/Return");

        // Initialize the Library and set the available books list
       List<Book> availableBooks = library.getAvailableBooks();
        availableBooksList.getItems().clear();
        availableBooksList.getItems().addAll(availableBooks);
       
       
        List<Book> myBooks = currentUser.getCheckedOutBooks();
        ObservableList<Book> observableList = FXCollections.observableArrayList(availableBooks);
        availableBooksList.setItems(observableList);
        if(myBooks!= null){       
            ObservableList<Book> myList = FXCollections.observableArrayList(myBooks);
        myBooksList.setItems(myList);
        }
        primaryStage.show();
        refreshLists();
        
    
        // Show the scene
        
    }

    public ClientGUI(User user, Library library){
        currentUser=user;
        this.library=library;
        availableBooksList= new ListView<Book>();
        myBooksList= new ListView<Book>();
        primaryStage= new Stage();
    }
    
    
    

    @FXML
    void handleCheckout(ActionEvent event) {
        // Get the selected book from the available books list
        Book book = availableBooksList.getSelectionModel().getSelectedItem();

        if (book != null) {
            // Checkout the book for the current user
            boolean b=currentUser.checkOutBook(book);
            if(!b){
                errorLabel.setText("You have been put on the waitlist");
            }

            // Refresh the available and my books lists
            refreshLists();
        }
    }

    @FXML
    void handleLogout(ActionEvent event) {
        // Clear the current user and show the login window
        
        DataFileManager.saveBooks(library.getBooks());
        DataFileManager.saveUsers(library.getUsers());
        currentUser = null;
        Platform.exit();     
    }

    @FXML
    void handleReturn(ActionEvent event) {
        // Get the book title to return from the text field
        String title = returnField.getText();
        Book book=myBooksList.getSelectionModel().getSelectedItem();

        if (book!=null) {
            // Find the book in the user's checked out books listand return it to the library
            boolean returnBook = currentUser.returnBook(book);       
             if (returnBook) {
                // Refresh the available and my books lists
                if(book.waitlist.size()>0){
                    User u=book.waitlist.remove(0);
                    u.waBooks().remove(book);
                    u.checkOutBook(book);
                }
                refreshLists();
            }
        } else if(!title.isEmpty()){
            boolean returnBook = currentUser.returnBook(title);       
            if (returnBook) {
               // Refresh the available and my books lists
               Book b= User.findBook(library.getBooks(),title);
               if(b.waitlist.size()>0){
                User u=book.waitlist.remove(0);
                u.waBooks().remove(book);
                u.checkOutBook(book);
            }
               returnField.clear();
               refreshLists();
           }
        } else{
            errorLabel.setText("Try again illegal return");
        }
    }
    
    @FXML
    void handleSearch(ActionEvent event) {
        // Get the search term from the search field
        String searchTerm = searchField.getText();
        searchField.clear();
        if (!searchTerm.isEmpty()) {
            // Search the library for books that match the search term
            List<Book> searchResults = library.searchBooks(searchTerm);
    
            if (!searchResults.isEmpty()) {
                // Set the available books list to the search results
                ObservableList<Book> observableList = FXCollections.observableArrayList(searchResults);
                availableBooksList.setItems(observableList);

            }
        } else {
            // If the search term is empty, reset the available books list to all available books
            List<Book> availableBooks = library.getAvailableBooks();
            ObservableList<Book> observableList = FXCollections.observableArrayList(availableBooks);
            availableBooksList.setItems(observableList);
        }
    }
    
    public void refreshLists() {
        // Refresh the available and my books lists
        List<Book> availableBooks = library.getAvailableBooks();
        List<Book> myBooks = currentUser.getCheckedOutBooks();
        ObservableList<Book> observableList = FXCollections.observableArrayList(availableBooks);
        availableBooksList.setItems(observableList);
        if(myBooks!= null){       
            ObservableList<Book> myList = FXCollections.observableArrayList(myBooks);
        myBooksList.setItems(myList);
        }
        // availableBooksList.setCellFactory(param -> new ListCell<Book>() {
        //     @Override
        //     protected void updateItem(Book item, boolean empty) {
        //         super.updateItem(item, empty);
        //         if (empty || item == null || item.getTitle() == null) {
        //             setText(null);
        //         } else {
        //             setText(item.getTitle());
        //         }
        //     }
        // });  
        // myBooksList.setCellFactory(param -> new ListCell<Book>() {
        //     @Override
        //     protected void updateItem(Book item, boolean empty) {
        //         super.updateItem(item, empty);
        //         if (empty || item == null || item.getTitle() == null) {
        //             setText(null);
        //         } else {
        //             setText(item.getTitle());
        //         }
        //     }
        // });
        

       // primaryStage.show();
    }
}    