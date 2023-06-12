package finalProj;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController {
    private ClientGUI clientGUI;
    private LibrarianGUI librarianGUI;
    private Client client;

    @FXML
    ImageView myImageView;

    @FXML
    private Label errorLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;
    @FXML 
    private ToggleButton librarianToggle;
    @FXML
    private Button loginButton;

    public void setClientGUI(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }
    public void setClient(Client client){
        this.client=client;
    }
    public void setImageView(Image view){
       myImageView.setImage(view);
    }

    @FXML
    void handleLogin(ActionEvent event) {
       
        String username = usernameField.getText();
        
        String password = passwordField.getText();
       

        boolean isLibrarian = librarianToggle.isSelected();
       System.out.println(username+" "+ password +" " + isLibrarian);


        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password");
            return;
        }
       

        Library library = new Library();
        System.out.println("reached");

        if (library.login(username, password,isLibrarian)) {
            System.out.println("reached");
            if(!isLibrarian){
               clientGUI=new ClientGUI(library.getCurrentUser(),library);
                try {
                    clientGUI.start(new Stage());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else{
                System.out.println("reached");
              librarianGUI= new LibrarianGUI(library.getCurrentUser(),library);
              try {

                librarianGUI.start(new Stage());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            }
        } else {
            System.out.println("reached");
            errorLabel.setText("Invalid username or password");
            usernameField.setText("");
            passwordField.setText("");
            librarianToggle.setSelected(false);
            library.destroy();
        }
    }
    @FXML
    void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        
        String password = passwordField.getText();
       

        boolean isLibrarian = librarianToggle.isSelected();
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password");
            return;
        }
        Library library = new Library();
        library.addUser(username, password, false);
        errorLabel.setText("Successfully Registered");
            usernameField.setText("");
            passwordField.setText("");
            library.destroy();
    }
}