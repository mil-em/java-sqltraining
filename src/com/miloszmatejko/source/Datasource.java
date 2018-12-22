package com.miloszmatejko.source;

import com.miloszmatejko.common.BookOfGenre;
import com.miloszmatejko.common.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {


    public static final String DB_NAME = "database.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;


    public static final String TABLE_GENRES = "genres";
    public static final String COLUMN_GENRE_ID = "genre_id";
    public static final String COLUMN_GENRE_NAME = "name";

    public static final String TABLE_BOOKS = "books";
    public static final String COLUMN_BOOK_ISBN = "ISBN";
    public static final String COLUMN_BOOK_TITLE = "title";
    public static final String COLUMN_BOOK_GENRE = "genre";

    public static final String QUERY_GENRES = "SELECT * FROM " + TABLE_GENRES;

    public static final String TABLE_BOOK_OF_GENRE_VIEW = "books_of_genre";

    public static final String CREATE_BOOK_OF_GENRE_VIEW = "CREATE VIEW IF NOT EXISTS " +
            TABLE_BOOK_OF_GENRE_VIEW + " AS SELECT " + TABLE_GENRES + "." + COLUMN_GENRE_NAME + ", " +
            TABLE_BOOKS + "." + COLUMN_BOOK_ISBN + ", " + TABLE_BOOKS + "." + COLUMN_BOOK_TITLE +
            " FROM " + TABLE_GENRES + " INNER JOIN " + TABLE_BOOKS + " ON " + TABLE_BOOKS + "." + COLUMN_BOOK_GENRE +
            " = " + TABLE_GENRES + "." + COLUMN_GENRE_ID;

    public static final String BOOK_OF_GENRE_VIEW_PREP = "SELECT * FROM " + TABLE_BOOK_OF_GENRE_VIEW + " WHERE " +
            COLUMN_BOOK_GENRE + " = ?";

    public static final String QUERY_GENRE = "SELECT " + COLUMN_GENRE_ID + " FROM " + TABLE_GENRES +
            " WHERE " + COLUMN_GENRE_NAME + " = ?";

    public static final String INSERT_GENRES = "INSERT INTO " + TABLE_GENRES +
            '(' + COLUMN_GENRE_NAME + ") VALUES (?)";


    public List<Genre> queryGenres() {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;


        try {
            connection = DriverManager.getConnection ( CONNECTION_STRING );
            statement = connection.createStatement ();
            resultSet = statement.executeQuery ( QUERY_GENRES );
            List<Genre> genres = new ArrayList<> ();
            while (resultSet.next ()) {
                Genre genre = new Genre ();
                genre.setGenre_id ( resultSet.getInt ( 1 ) );
                genre.setGenre_name ( resultSet.getString ( 2 ) );
                genres.add ( genre );
            }
            return genres;
        } catch (SQLException e) {
            System.out.println ( "Exception while querying genres " + e.getMessage () );
            return null;
        } finally {
            try {
                if (statement != null) {
                    statement.close ();
                }
                if (resultSet != null) {
                    resultSet.close ();
                }
                if (connection != null)
                    connection.close ();
            } catch (SQLException e) {
                System.out.println ( "Unable to close something" + e.getMessage () );
            }
        }
    }


    public List<BookOfGenre> queryBooksOfGenre(String genreName) {


        Connection connection = null;
        PreparedStatement queryBookOfGenreView = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection ( CONNECTION_STRING );
            queryBookOfGenreView = connection.prepareStatement ( BOOK_OF_GENRE_VIEW_PREP );
            queryBookOfGenreView.setString ( 1, genreName );
            resultSet = queryBookOfGenreView.executeQuery ();
            List<BookOfGenre> booksOfGenre = new ArrayList<> ();
            while (resultSet.next ()) {
                BookOfGenre bookOfGenre = new BookOfGenre ();
                bookOfGenre.setGenre ( resultSet.getString ( 1 ) );
                bookOfGenre.setISBN ( resultSet.getString ( 2 ) );
                bookOfGenre.setTitle ( resultSet.getString ( 3 ) );
                booksOfGenre.add ( bookOfGenre );
            }
            return booksOfGenre;
        } catch (SQLException e) {
            System.out.println ( "Exception while querying booksOfGenre" + e.getMessage () );
            return null;
        } finally {
            try {
                if (queryBookOfGenreView != null)
                    queryBookOfGenreView.close ();
                if (resultSet != null)
                    resultSet.close ();
                if (connection != null)
                    connection.close ();
            } catch (SQLException e) {
                System.out.println ( "Unable to close something" + e.getMessage () );
            }

        }

    }


    public int insertIntoGenres(String name) throws SQLException {

        Connection connection = null;
        PreparedStatement queryGenre = null;
        PreparedStatement insertIntoGenres = null;
        ResultSet resultSet = null;
        ResultSet generatedKeys = null;

        try {
            connection = DriverManager.getConnection ( CONNECTION_STRING );
            queryGenre = connection.prepareStatement ( QUERY_GENRE );
            queryGenre.setString ( 1, name );
            resultSet = queryGenre.executeQuery ();
            if (resultSet.next ()) {
                return resultSet.getInt ( 1 );
            } else {
                insertIntoGenres = connection.prepareStatement ( INSERT_GENRES, Statement.RETURN_GENERATED_KEYS );
                insertIntoGenres.setString ( 1, name );
                int affectedRows = insertIntoGenres.executeUpdate ();
                if (affectedRows != 1) {
                    throw new SQLException ( "Couldn't insert Genre!" );
                }
                generatedKeys = insertIntoGenres.getGeneratedKeys ();
                if (generatedKeys.next ()) {
                    return generatedKeys.getInt ( 1 );
                } else {
                    throw new SQLException ( "Couldn't get id for Genre" );
                }
            }
        } catch (SQLException e) {
            System.out.println ( "Exception in insert iIntoGenres method " + e.getMessage () );
            return -1;
        } finally {
            try {
                if (generatedKeys != null)
                    generatedKeys.close ();
                if (resultSet != null)
                    resultSet.close ();
                if (insertIntoGenres != null)
                    insertIntoGenres.close ();
                if (queryGenre != null)
                    queryGenre.close ();
                if (connection != null)
                    connection.close ();
            } catch (SQLException e) {
                System.out.println ( "Unable to close something " + e.getMessage () );
            }
        }
    }

    private void insertBook(String name, String genre) {

    }

    private void updateData(String bookName, String newIsbn, String newBookName, String genre) {

        //genre id is gonna be returned from insertGenre
    }
}
