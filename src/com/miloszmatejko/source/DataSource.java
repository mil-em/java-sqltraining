package com.miloszmatejko.source;

import com.miloszmatejko.common.BookOfGenre;
import com.miloszmatejko.common.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSource {


    private static final String DB_NAME = "database.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;


    private static final String TABLE_GENRES = "genres";
    private static final String COLUMN_GENRE_ID = "genre_id";
    private static final String COLUMN_GENRE_NAME = "name";

    private static final String TABLE_BOOKS = "books";
    private static final String COLUMN_BOOK_ISBN = "ISBN";
    private static final String COLUMN_BOOK_TITLE = "title";
    private static final String COLUMN_BOOK_GENRE = "genre";

    private static final String QUERY_GENRES = "SELECT * FROM " + TABLE_GENRES;

    private static final String TABLE_BOOK_OF_GENRE_VIEW = "books_of_genre";

    private static final String CREATE_BOOK_OF_GENRE_VIEW = "CREATE VIEW IF NOT EXISTS " +
            TABLE_BOOK_OF_GENRE_VIEW + " AS SELECT " + TABLE_GENRES + "." + COLUMN_GENRE_NAME + ", " +
            TABLE_BOOKS + "." + COLUMN_BOOK_ISBN + ", " + TABLE_BOOKS + "." + COLUMN_BOOK_TITLE +
            " FROM " + TABLE_GENRES + " INNER JOIN " + TABLE_BOOKS + " ON " + TABLE_BOOKS + "." + COLUMN_BOOK_GENRE +
            " = " + TABLE_GENRES + "." + COLUMN_GENRE_ID;

    private static final String BOOK_OF_GENRE_VIEW_PREP = "SELECT * FROM " + TABLE_BOOK_OF_GENRE_VIEW + " WHERE " +
            COLUMN_BOOK_GENRE + " = ?";

    private static final String QUERY_GENRE = "SELECT " + COLUMN_GENRE_ID + " FROM " + TABLE_GENRES +
            " WHERE " + COLUMN_GENRE_NAME + " = ?";

    private static final String INSERT_GENRES = "INSERT INTO " + TABLE_GENRES +
            '(' + COLUMN_GENRE_NAME + ") VALUES (?)";

    private static final String INSERT_BOOKS = "INSERT INTO " + TABLE_BOOKS +
            '(' + COLUMN_BOOK_ISBN + ", " + COLUMN_BOOK_TITLE + ", " + COLUMN_BOOK_GENRE + ") VALUES(?, ?, ?)";

    private static final String UPDATE_BOOKS = "UPDATE " + TABLE_BOOKS + " SET " + COLUMN_BOOK_ISBN + " = ?, " +
            COLUMN_BOOK_TITLE + " = ?, " + COLUMN_BOOK_GENRE +
            " = ? WHERE " + COLUMN_BOOK_TITLE + " = ?";
    private static final String DELETE_BOOKS = "DELETE FROM " + TABLE_BOOKS + " WHERE "+ COLUMN_BOOK_TITLE +" = ?";

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

    public void insertIntoBooks(String isbn, String name, String genre){
        Connection connection = null;
        PreparedStatement insertIntoBooks = null;
        try {
            connection = DriverManager.getConnection ( CONNECTION_STRING );
            connection.setAutoCommit ( false );
            int genreId = insertIntoGenres ( genre );
            insertIntoBooks = connection.prepareStatement ( INSERT_BOOKS );
            insertIntoBooks.setString ( 1, isbn );
            insertIntoBooks.setString ( 2, name );
            insertIntoBooks.setInt ( 3, genreId );
            int affectedRows = insertIntoBooks.executeUpdate ();
            if (affectedRows == 1) {
                connection.commit ();
            } else {
                throw new SQLException ( "The book insert failed" );
            }
        } catch (SQLException e) {
            System.out.println ( "Exception in insertBook method " + e.getMessage () );
            try {
                System.out.println ( "performing rollback" );
                if (connection != null)
                    connection.rollback ();
            } catch (SQLException e2) {
                System.out.println ( "Exception while performing rollback" + e.getMessage () );
            }
        } finally {
            try {
                System.out.println ( "Resetting default commit behavior." );
                connection.setAutoCommit ( true );
                if (insertIntoBooks != null)
                    insertIntoBooks.close ();
                if (connection != null)
                    connection.close ();
            } catch (SQLException e) {
                System.out.println ( "couldn't reset auto commit" );
            }
        }

    }

    public void updateBook(String bookOldName, String newIsbn, String newBookTitle, String newGenre) {

        Connection connection = null;
        PreparedStatement updateBook = null;

        try {
            connection = DriverManager.getConnection ( CONNECTION_STRING );
            connection.setAutoCommit ( false );
            updateBook = connection.prepareStatement ( UPDATE_BOOKS );
            System.out.println ("updateBook = " + updateBook.toString ());
            updateBook.setString ( 1, newIsbn );
            updateBook.setString ( 2, newBookTitle );
            int genreId = insertIntoGenres ( newGenre );
            updateBook.setInt ( 3, genreId );
            updateBook.setString ( 4, bookOldName );
            System.out.println ("updateBook after setString" + updateBook.toString ());
            int affectedRows  = updateBook.executeUpdate ();
            System.out.println ("affected rows = "+ affectedRows);
            if (affectedRows == 1) {
                connection.commit ();
            } else {
                throw new SQLException ( "update failed" );
            }
        } catch (SQLException e ) {
            try {
                System.out.println ( "Exception in updateBook " + e.getMessage () );
                connection.rollback ();
            } catch (SQLException e1) {
                System.out.println ("couldn't perform a rollback" + e.getMessage ());
            }
        }finally {
            try {
                System.out.println ("setting autocommit default true ");
                    connection.setAutoCommit ( true );
                if (updateBook != null) {
                    updateBook.close ();
                }
                if (connection!=null) {
                    connection.close ();
                }
            } catch (SQLException e) {
                System.out.println ( "setting autocommit true failed" + e.getMessage () );
            }
        }
    }

    public void deleteFromBooks(String bookTitle) throws SQLException {
        Connection connection = null;
        PreparedStatement deleteFromBooks = null;

        try {
            connection = DriverManager.getConnection ( CONNECTION_STRING );
            deleteFromBooks = connection.prepareStatement ( DELETE_BOOKS );
            deleteFromBooks.setString ( 1, bookTitle );
            int affectedRows = deleteFromBooks.executeUpdate ();
            if (affectedRows <1) {
                throw new SQLException ( "Deleted Nothing!" );
            }
        } catch (SQLException e) {
            System.out.println ( "Exception in deleteFromBooks " + e.getMessage () );
        }finally {
            try {
                if (deleteFromBooks != null) {
                    deleteFromBooks.close ();
                }
                if (connection != null) {
                    connection.close ();
                }
            } catch (SQLException e) {
                System.out.println ("unable to close something after deleteFromBooks" + e.getMessage ());
            }
        }

    }

}
