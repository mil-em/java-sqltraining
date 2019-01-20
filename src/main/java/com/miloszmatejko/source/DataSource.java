package com.miloszmatejko.source;

import com.miloszmatejko.source.common.BookOfGenre;
import com.miloszmatejko.source.common.DataSourceException;
import com.miloszmatejko.source.common.Genre;
import com.miloszmatejko.source.common.SimpleBookDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSource implements SimpleBookDatabase {


    private static final String DB_NAME = "database.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:src/main/resources/" + DB_NAME;


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

    private Connection connection;

    private PreparedStatement queryBookOfGenreView;
    private PreparedStatement queryGenre;
    private PreparedStatement insertIntoGenres;
    private PreparedStatement insertIntoBooks;
    private PreparedStatement updateBook;
    private PreparedStatement deleteFromBooks;




    public void open() throws DataSourceException {
        try {
            connection = DriverManager.getConnection ( CONNECTION_STRING );
            queryBookOfGenreView = connection.prepareStatement ( BOOK_OF_GENRE_VIEW_PREP );
            queryGenre = connection.prepareStatement ( QUERY_GENRE );
            insertIntoGenres = connection.prepareStatement ( INSERT_GENRES, Statement.RETURN_GENERATED_KEYS );
            insertIntoBooks = connection.prepareStatement ( INSERT_BOOKS );
            updateBook = connection.prepareStatement ( UPDATE_BOOKS );
            deleteFromBooks = connection.prepareStatement ( DELETE_BOOKS );

        } catch (SQLException e) {
            throw new DataSourceException ( "couldn't connect to the database " + e.getMessage () );

        }
    }


    public void close() throws DataSourceException {
        try {
            if (queryBookOfGenreView != null) {
                queryBookOfGenreView.close ();
            }
            if (queryGenre != null) {
                queryGenre.close ();
            }
            if (insertIntoGenres != null) {
                insertIntoGenres.close ();
            }
            if (insertIntoBooks != null) {
                insertIntoBooks.close ();
            }
            if (updateBook != null) {
                updateBook.close ();
            }
            if (deleteFromBooks != null) {
                deleteFromBooks.close ();
            }
        } catch (SQLException e) {
            throw new DataSourceException ("unable to close something " + e.getMessage ());
        }
    }

    public List<Genre> queryGenres() throws DataSourceException {


        try (Statement statement = connection.createStatement ();
             ResultSet resultSet = statement.executeQuery ( QUERY_GENRES )) {

            List<Genre> genres = new ArrayList<> ();
            while (resultSet.next ()) {
                Genre genre = new Genre ();
                genre.setGenre_id ( resultSet.getInt ( 1 ) );
                genre.setGenre_name ( resultSet.getString ( 2 ) );
                genres.add ( genre );
            }
            return genres;
        } catch (SQLException e) {
            throw new DataSourceException ( "Exception while querying genres " + e.getMessage () );
        }
    }


    public List<BookOfGenre> queryBooksOfGenre(String genreName) throws DataSourceException {
        ResultSet resultSet=null;
        try{
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
            throw new DataSourceException ( "Exception while querying booksOfGenre" + e.getMessage () );

        } finally {
            try {
                 if (resultSet != null)
                    resultSet.close ();
            } catch (SQLException e) {
                System.out.println ( "Unable to close something" + e.getMessage () );
            }

        }

    }


    public int insertIntoGenres(String genreName) throws DataSourceException {


        ResultSet resultSet = null;
        ResultSet generatedKeys = null;

        try {
            queryGenre.setString ( 1, genreName );
            resultSet = queryGenre.executeQuery ();
            if (resultSet.next ()) {
                return resultSet.getInt ( 1 );
            } else {
                insertIntoGenres.setString ( 1, genreName );
                int affectedRows = insertIntoGenres.executeUpdate ();
                if (affectedRows != 1) {
                    throw new DataSourceException ( "Couldn't insert Genre!" );
                }
                generatedKeys = insertIntoGenres.getGeneratedKeys ();
                if (generatedKeys.next ()) {
                    return generatedKeys.getInt ( 1 );
                } else {
                    throw new DataSourceException ( "Couldn't get id for Genre" );
                }
            }
        } catch (SQLException e) {
            throw new DataSourceException ( "Exception in insert iIntoGenres method " + e.getMessage () );
        } finally {
            try {
                if (generatedKeys != null)
                    generatedKeys.close ();
                if (resultSet != null)
                    resultSet.close ();
                } catch (SQLException e) {
                throw new DataSourceException ( "Unable to close something " + e.getMessage () );
            }
        }
    }

    public void insertIntoBooks(String isbn, String name, String genre) throws DataSourceException{

        try {
            connection.setAutoCommit ( false );
            insertIntoBooks.setString ( 1, isbn );
            insertIntoBooks.setString ( 2, name );
            int genreId = insertIntoGenres ( genre );
            insertIntoBooks.setInt ( 3, genreId );
            int affectedRows = insertIntoBooks.executeUpdate ();
            if (affectedRows == 1) {
                connection.commit ();
            } else {
                throw new DataSourceException ( "The book insert failed" );
            }
        } catch (SQLException e) {
             try {
//                System.out.println ( "performing rollback" );
                if (connection != null)
                    connection.rollback ();
                throw new DataSourceException ( "The book insert failed" + e.getMessage () );
            } catch (SQLException e2) {
                throw new DataSourceException ( "Exception while performing rollback" + e.getMessage () );
            }
        } finally {
            try {
//                System.out.println ( "Resetting default commit behavior." );
                connection.setAutoCommit ( true );
            } catch (SQLException e) {
                throw new DataSourceException ( "couldn't reset auto commit" );
            }
        }

    }

    public void updateBook(String bookOldName, String newIsbn, String newBookTitle, String newGenre) throws DataSourceException {
        try {

            connection.setAutoCommit ( false );
            updateBook.setString ( 1, newIsbn );
            updateBook.setString ( 2, newBookTitle );
            int genreId = insertIntoGenres ( newGenre );
            updateBook.setInt ( 3, genreId );
            updateBook.setString ( 4, bookOldName );

            int affectedRows  = updateBook.executeUpdate ();
            if (affectedRows == 1) {
                connection.commit ();
            } else {
                throw new DataSourceException ( "update failed" );
            }
        } catch (SQLException e ) {
            try {
                System.out.println ( "Exception in updateBook " + e.getMessage () );
                connection.rollback ();
            } catch (SQLException e1) {
                throw new DataSourceException ("couldn't perform a rollback" + e.getMessage ());
            }
        }finally {
            try {
//                System.out.println ("setting autocommit default true ");
                    connection.setAutoCommit ( true );
            } catch (SQLException e) {
                throw new DataSourceException ( "setting autocommit true failed" + e.getMessage () );
            }
        }
    }

    public void deleteFromBooks(String bookTitle) throws DataSourceException {

        try {
            deleteFromBooks.setString ( 1, bookTitle );
            int affectedRows = deleteFromBooks.executeUpdate ();
            if (affectedRows <1) {
                throw new DataSourceException ( "Deleted Nothing!" );
            }
        } catch (SQLException e) {
           throw new DataSourceException ( "Exception in deleteFromBooks " + e.getMessage () );

    }
}
}
