package finalProj;

import java.io.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.scene.chart.PieChart.Data;

public class DataFileManager {
    private static final String USERS_FILE = "users.txt";
    private static final String BOOKS_FILE = "books.txt";
     static List<User> users = new ArrayList<User>();
     static List<Book> books = new ArrayList<Book>();

    public DataFileManager(){
        loadBooks();
        loadUsers();
    }
    public  List<User> getUsers(){
        return users;
    }
    public static  List<Book> getBooks(){
        return books;
    }

    public  List<User> loadUsers() {
        
        try (Scanner in = new Scanner(new FileInputStream(USERS_FILE))) {
            while(in.hasNextLine()){
                
                String[] lin=in.nextLine().split(",");
                if(lin==null){
                    return users;
                }
                users.add(new User(lin[0], lin[1], Boolean.valueOf(lin[2]), lin[3], lin[4]));
            }
        } catch (FileNotFoundException e) {
            // Do nothing; it's okay if the file doesn't exist yet
            System.out.println("filenotfound");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public  List<Book> loadBooks() {
        
        try (Scanner in = new Scanner(new FileInputStream(BOOKS_FILE))) {
           
            while(in.hasNext()){
                String[] lin=in.nextLine().split(",");
                if(lin==null){
                    return books;
                }
                // User user=null;
                // if(!lin[4].contains("null")){
                //    for(User u: users){
                //     if(u.getUsername().equals(lin[4])){
                //         user=u;
                //     }
                //    }
                // }
                books.add( new Book(lin[0], lin[1], lin[2], Boolean.parseBoolean(lin[3]), null, lin[5],Integer.parseInt(lin[6]) ));
            }
        } catch (FileNotFoundException e) {
            // Do nothing; it's okay if the file doesn't exist yet
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }

    public static void saveUsers(List<User> userstoOutput) {
        try (PrintWriter out = new PrintWriter(new FileOutputStream(USERS_FILE))) {
        
            for(User user:userstoOutput){
                StringBuilder sb= new StringBuilder();
                StringBuilder sj= new StringBuilder();
                sj.append("[");
                sb.append("[");
                if(user.getCheckedOutBooks().size()>0){
                    for(int cntr=0; cntr<user.getCheckedOutBooks().size(); cntr++){
                        if(cntr==user.getCheckedOutBooks().size()-1){
                            sb.append(user.getCheckedOutBooks().get(cntr));
                        }else{
                            sb.append(user.getCheckedOutBooks().get(cntr) +" | ");
                        }
                    }
                } else{
                    sb.append("null");
                }
                if(user.waBooks().size()>0){
                    for(int cntr=0; cntr<user.waBooks().size(); cntr++){
                        if(cntr==user.waBooks().size()-1){
                            sj.append(user.waBooks().get(cntr));
                        }else{
                            sj.append(user.waBooks().get(cntr) +" | ");
                        }
                    }
                }else{
                    sj.append("null");
                }
                
                sb.append("]");
                sj.append("]");
                String pass=User.decrypString(user.getPassword());
                out.println(user.getUsername() +","+pass+"," + String.valueOf(user.isLibrarian())+ ","+ sb.toString()+ ","+ sj.toString());
            }
            out.close();         
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void saveBooks(List<Book> booksToOut) {
        try (PrintWriter out = new PrintWriter(new FileOutputStream(BOOKS_FILE))) {
        
            for(Book b:booksToOut){
                StringBuilder sb= new StringBuilder();
                sb.append("[");
                if(b.waitlist.size()>0){
                    for(int cntr=0; cntr<b.waitlist.size(); cntr++){
                        if(cntr==b.waitlist.size()-1){
                            sb.append(b.waitlist.get(cntr).getUsername());
                        }else{
                            sb.append(b.waitlist.get(cntr).getUsername() +"|");
                        }
                    }
                } else{
                    sb.append("null");
                }
                sb.append("]");

                if(b.checkedOutBy==null){
                    out.println((b.getTitle() + "," +b.getAuthor() + "," +b.getISBN()+ "," + String.valueOf(b.isCheckedOut())+ "," + null+ ","+ sb.toString()+","+ Math.abs(b.getDueDate())));
                }else{
                out.println((b.getTitle() + "," +b.getAuthor() + "," +b.getISBN()+ "," + String.valueOf(b.isCheckedOut())+ "," + b.checkedOutBy.getUsername()+ ","+ sb.toString() +","+ Math.abs(b.getDueDate())));
                } 
            }
            out.close();  
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
