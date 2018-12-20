package com.miloszmatejko.source;

import com.miloszmatejko.common.Book_genre;
import com.miloszmatejko.common.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class Datasource {


    public static final String DB_NAME = "database.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\milosz\\projects\\java-sqltraining\\database.db";


    public static final String TABLE_BOOK_GENRES = "book_genres";
    public static final String COLUMN_GENRE_ID = "genre_id";
    public static final String COLUMN_GENRE_NAME = "genre_name";

    public static final String TABLE_BOOKS = "books";
    public static final String COLUMN_BOOK_ISBN = "book_ISBN";
    public static final String COLUMN_BOOK_NAME = "book_name";
    public static final String COLUMN_GENRE = "genre";


    private Connection connection;


    private PreparedStatement insertIntoGenres;
    private PreparedStatement insertIntoBooks;
    private PreparedStatement queryGenre;
    private PreparedStatement queryBook;


    private List<Book_genre> showAllGenres() {

        List<Book_genre> genres = new ArrayList<> ();

        return genres;
    }

    private List<Book> queryBooksOfGenre(String genre) {
        List<Book> books = new ArrayList<> ();
        return books;
    }



    private int insertGenre(String name) {
        return 0;
    }

    private void insertBook(String name, String genre) {

    }

    private void updateData(String bookName, String newIsbn, String newBookName, String genre) {

        //genre id is gonna be returned from insertGenre
    }
}
