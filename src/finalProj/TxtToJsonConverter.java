package finalProj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TxtToJsonConverter {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("users.txt"));
            JSONArray booksArray = new JSONArray();
            String line;
            while ((line = br.readLine()) != null) {
                String[] bookInfo = line.split(",");
                JSONObject book = new JSONObject();
                book.put("username", bookInfo[0]);
                book.put("password", bookInfo[1]);
                book.put("isLibrarian", bookInfo[2]);
                booksArray.add(book);
            }
            br.close();

            FileWriter fw = new FileWriter("users.json");
            fw.write(booksArray.toJSONString());
            fw.flush();
            fw.close();
            System.out.println("Successfully converted text file to JSON.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
