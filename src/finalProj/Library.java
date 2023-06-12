package finalProj;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.PieChart.Data;

public class Library {
    private static List<Book> books;
    private List<User> users;
    private User loggedInUser;
    DataFileManager dataFileManager;
    public Library(List<Book> books, List<User> users) {
        this.books = books;
        this.users = users;
        this.loggedInUser = null;
    }
    public Library(){
         dataFileManager= new DataFileManager();
        users=DataFileManager.users;
        books=DataFileManager.books;
    }
    public void destroy(){
        if(dataFileManager!=null){
            DataFileManager.users=new ArrayList<>();
            DataFileManager.books=new ArrayList<>();
            dataFileManager=null;
        }
    }
    
    
    
    public boolean login(String username, String password, boolean isLibrarian) {
        // Search for the user in the list of users
         System.out.println("reached");
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(User.encrypString(password)) && user.isLibrarian()==isLibrarian) {
                loggedInUser = user;
                return true;
            }
        }
        
        return false;
    }
    
    public void register(String username, String password, boolean isLibrarian) {
        // Create a new user with the given username and password
        User user = new User(username, password, isLibrarian,"[null]","[null]");
        users.add(user);
    }

    public  List<Book> getBooks() {
        return books;
    }
    
    public List<User> getUsers() {
        return users;
    }
    
    public User getLoggedInUser() {
        return loggedInUser;
    }



    public List<Book> searchBooks(String keyword) {
        List<Book> matchingBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) || book.getAuthor().toLowerCase().contains(keyword.toLowerCase()) || book.getISBN().toLowerCase().contains(keyword.toLowerCase())) {
                matchingBooks.add(book);
            }
        }
        return matchingBooks;
    }
    public List<Book> getAvailableBooks() {
        
        return books;
    }
    public User getCurrentUser() {
        return loggedInUser;
    }
    public void addBook(Book book) {
        books.add(0, book);
        DataFileManager.saveBooks(books);
    }
    public boolean addUser(String username, String password,boolean isLibrarian) {
        boolean b= users.add(new User(username, password, isLibrarian, "[null]","[null]"));
        DataFileManager.saveUsers(users);
        return b;
    }
    public boolean removeBook(String title, String author, String isbn) {
        for(Book b: books){
            if(b.getTitle().toLowerCase().equals(title.toLowerCase()) && b.getAuthor().toLowerCase().equals(author.toLowerCase()) && b.getISBN().equals(isbn)){
                books.remove(b);
                DataFileManager.saveBooks(books);
                return true;
            }
            } 
        
        return false;
        }
    
    public boolean removeUser(String username) {
        for(User u: users){
            if(u.getUsername().equals(username)){
                users.remove(u);
                DataFileManager.saveUsers(users);
                return true;
            }
            } 
        
        return false;
        }
    }