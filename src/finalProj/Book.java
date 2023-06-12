package finalProj;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.PieChart.Data;

public class Book {
    private String title;
    private String author;
    private String ISBN;
    private boolean checkedOut;
    public User checkedOutBy;
    private LocalDate dueDate;
    public List<User> waitlist;

    public Book(String title, String author, String ISBN,boolean checkedOut, User username, String list, int date) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.checkedOut = checkedOut;
        this.checkedOutBy = username;
       
    waitlist = new ArrayList<User>();
    dueDate=(LocalDate.now().plusDays(date));

    }
    public String getTitle() {
        return title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getISBN() {
        return ISBN;
    }
    
    

    public boolean isCheckedOut() {
        return checkedOut;
    }
    public void setCheckedOut(boolean b) {
        checkedOut=b;
    }
    public void setDueDate(LocalDate plusDays) {
        dueDate=plusDays;
    }
    public void setCheckedOutBy(User user) {
        checkedOutBy=user;
    }
    public User getCheckedOutBy() {
        return checkedOutBy;
    }
    public int getDueDate() {
        
        long epochDays =  ChronoUnit.DAYS.between(dueDate, LocalDate.now());;
        return (int) epochDays;
    }
    public LocalDate getDate(){
        return dueDate;
    }

    @Override
    public String toString(){
        return title;

    }
    
   
}