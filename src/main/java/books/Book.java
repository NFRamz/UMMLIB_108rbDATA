package books;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;


public class Book {

    //==================================== ATRIBUT ====================================
    private String nim;
    private String bookId;
    private String title;
    private String author;
    private String category;
    private int stock;
    private int duration;
    private LocalDate expired_borrowedBook;

    //ArrayList untuk menyimpan list buku yang terdaftar.
    //public static ArrayList<Book> arr_bookList = new ArrayList<>();

    //ArrayList untuk menyimpan list buku yang sedang dipinjam mahasiswa.
    //public static ArrayList<Book> arr_borrowedBook = new ArrayList<>();

    // ObservableList
    public static ObservableList<Book> arr_bookList = FXCollections.observableArrayList();
    public static ObservableList<Book> arr_borrowedBook = FXCollections.observableArrayList();
    //====================================== METHOD ======================================

    // 4 Method konstruktor dengan nama yang sama, bertujuan untuk menerapkan fungsi overloading (Modul 3)
    public Book(){

    }

    public Book(String category){
        this.category = category;
    }


    public Book(String bookId, String title, String author,String category, int stock){
        this.bookId   = bookId;
        this.title    = title;
        this.author   = author;
        this.category = category;
        this.stock    = stock;

    }


    //new for database
    public Book(String nim, String bookId, String title, String author, String category, int duration) {
        this.nim = nim;
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.duration = duration;
        this.expired_borrowedBook = LocalDate.now().plusDays(duration);
    }

    public Book(String nim, String bookId, String title, String author, String category, int duration, LocalDate expired_borrowedBook) {
        this.nim = nim;
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.duration = duration;
        this.expired_borrowedBook = LocalDate.now().plusDays(duration);
    }


    //=================================== SETTER METHOD ====================================
    public void setNim(String nim){
        this.nim = nim;
    }

    public void setBookId(String bookId){
        this.bookId     = bookId;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setAuthor(String author){
        this.author = author;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public void setStock(int stock){
        this.stock = stock;
    }
    public void setDuration(int duration){
        this.duration = duration;
    }




    //=================================== GETTER METHOD ==================================
    public String getNim(){
        return nim;
    }
    public String getBookId(){
        return bookId;
    }
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public String getCategory(){
        return category;
    }
    public int getStock(){
        return stock;
    }
    public int getDuration(){
        return duration;
    }
    public LocalDate getExpired_borrowedBook() {
        return expired_borrowedBook;
    }

}
