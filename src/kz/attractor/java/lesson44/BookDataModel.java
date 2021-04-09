package kz.attractor.java.lesson44;

import java.util.ArrayList;
import java.util.List;

import static kz.attractor.java.lesson44.Status.FREE;

public class BookDataModel {
    private List<Book> books= new ArrayList<>();

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public BookDataModel() {
            books.add(new Book("Celestial Bodies","Jokha Alharthi","Detective"));
            books.add(new Book("Cheque book","Vasdev Mohi","Romantic"));
            books.add(new Book("The Overstory","TRichard Powers","History"));
            books.add(new Book("Viswanathan Anand and Susan Ninan","Mind-Master","MakeSelf"));
        }
    public class Book{
        private String name;
        private String author;
        private String genre;
        private Status status = FREE;




        public Book(String name) {
            this.name = name;
        }

        public Book(String name, String author, String genre) {
            this.name = name;
            this.author = author;
            this.genre = genre;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }
    }
}

