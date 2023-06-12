package finalProj;

import java.nio.charset.StandardCharsets;
import java.nio.file.WatchEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.gluonhq.impl.charm.a.b.b.t;

public class User {
    private String username;
    private String password;
    private boolean librarian;
    private List<Book> checkedOutBooks;
    private List<Book> waList;


    public User(String username, String password, boolean librarian, String list, String waListString) {
        List<Book> books= DataFileManager.getBooks();
        
        this.username = username;
        this.password = encrypString(password);
        this.librarian = librarian;
        checkedOutBooks = new ArrayList<Book>();
        list = list.substring(1, list.length() - 1);
        String[] elements = list.split("\\|");
        
        for (String element : elements) {
            if(!element.equals("null")){
                Book b=findBook(books, element.trim());
                b.setCheckedOutBy(this);
                checkedOutBooks.add(b);
            }
            
        }
        waListString=waListString.substring(1, waListString.length() - 1);
        String[] elementsTwo = waListString.split("\\|");
        waList= new ArrayList<>();
        
        for (String element : elementsTwo) {
            if(!element.equals("null")){
                Book b=findBook(books, element.trim());
                b.waitlist.add(this);
                waList.add(b);
            }
            
        }

    }
    public static Book findBook(List<Book> books, String title){
        for(Book b: books){
            if(b.getTitle().toLowerCase().equals(title.toLowerCase())){
                return b;
            }
        }
        return null;
    }

    public void addWalist(Book b){
        waList.add(b);

    }
    public List<Book> waBooks() {
        return waList;
    }
    
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public boolean isLibrarian() {
        return librarian;
    }
    
    public List<Book> getCheckedOutBooks() {
        return checkedOutBooks;
    }

    public static String encrypString(String password){
        StringBuilder sb= new StringBuilder(password);
        sb.append("c");
        sb.reverse();
        sb.append("d");
        sb.insert(password.length()/2, 'f');
        sb.reverse();
        return sb.toString();
    }
    public static String decrypString(String password){
        StringBuilder sb = new StringBuilder(password);
        sb.deleteCharAt(0); // remove 'd'
        sb.deleteCharAt(password.length() / 2); // remove 'f'
        sb.reverse();
        sb.deleteCharAt(0); // remove 'c'
        sb.reverse();
        return sb.toString();
    }
    
    
    
    public boolean checkOutBook(Book book) {
        if (!book.isCheckedOut()) {
            book.setCheckedOut(true);
            book.setCheckedOutBy(this);
            book.setDueDate(LocalDate.now().plusDays(14));
            if(checkedOutBooks==null){
                checkedOutBooks=new ArrayList<>();
            }
            this.checkedOutBooks.add(book);
            return true;
        } else {
            book.waitlist.add(this);
            waList.add(book);
            return false;
        }
    }
    
    public boolean returnBook(Book book) {
        if (book.isCheckedOut() && book.getCheckedOutBy() == this) {
            book.setCheckedOut(false);
            book.setCheckedOutBy(null);
            book.setDueDate(null);
            checkedOutBooks.remove(book);
            return true;
        } else {
            return false;
        }
    }
    public boolean returnBook(String keyword) {
        for(Book book: checkedOutBooks){
        if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) || book.getAuthor().toLowerCase().contains(keyword.toLowerCase()) || book.getISBN().toLowerCase().contains(keyword.toLowerCase())) {
            if (book.isCheckedOut() && book.getCheckedOutBy() == this) {
                book.setCheckedOut(false);
                book.setCheckedOutBy(null);
                book.setDueDate(null);
                checkedOutBooks.remove(book);
                // return true;
            } else {
                return false;
            }
        }
    }
        return true;
    }
    
    public boolean extendDueDate(Book book) {
        if (book.isCheckedOut() && book.getCheckedOutBy() == this && book.waitlist.isEmpty()) {
            book.setDueDate(book.getDate().plusDays(7));
            return true;
        } else {
            return false;
        }
    }
}