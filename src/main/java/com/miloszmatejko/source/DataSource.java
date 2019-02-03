package com.miloszmatejko.source;

import com.miloszmatejko.model.BookOfGenre;
import com.miloszmatejko.model.DataSourceException;
import com.miloszmatejko.model.Genre;
import com.miloszmatejko.model.SimpleBookDatabase;

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
    private static final String DELETE_BOOKS = "DELETE FROM " + TABLE_BOOKS + " WHERE " + COLUMN_BOOK_TITLE + " = ?";


    private SourceUtils sourceUtils = new SourceUtils ();


    public List<Genre> queryAllGenres() throws DataSourceException {

        try (Connection connection = DriverManager.getConnection ( CONNECTION_STRING );
             Statement statement = connection.createStatement ();
             ResultSet resultSet = statement.executeQuery ( QUERY_GENRES )) {

            List<Genre> genres = new ArrayList<> ();
            while (resultSet.next ()) {

                int genreId = (resultSet.getInt ( 1 ));
                String genreName = (resultSet.getString ( 2 ));
                Genre genre = Genre.createGenre ( genreId, genreName );
                genres.add ( genre );
            }
            return genres;
        } catch (SQLException e) {
            throw new DataSourceException ( "Exception while querying genres ", e );
        }
    }


    public List<BookOfGenre> queryBooksOfGenre(String genreName) throws DataSourceException {
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
                String genre = (resultSet.getString ( 1 ));
                String iSBN = (resultSet.getString ( 2 ));
                String title = (resultSet.getString ( 3 ));
                BookOfGenre bookOfGenre = BookOfGenre.createBookOfGenre ( genre,iSBN, title );
                booksOfGenre.add ( bookOfGenre );
            }
            return booksOfGenre;
        } catch (SQLException e) {
            throw new DataSourceException ( "Exception while querying booksOfGenre", e );

        } finally {
            sourceUtils.closeResources ( resultSet );
            sourceUtils.closeResources ( queryBookOfGenreView );
            sourceUtils.closeResources ( connection );
        }
    }


    public void insertIntoGenres(String genreName) throws DataSourceException {

        Connection connection = null;
        PreparedStatement queryGenre = null;
        ResultSet resultSet = null;
        PreparedStatement insertIntoGenres = null;

        try {
            connection = DriverManager.getConnection ( CONNECTION_STRING );
            queryGenre = connection.prepareStatement ( QUERY_GENRE );
            queryGenre.setString ( 1, genreName );
            resultSet = queryGenre.executeQuery ();
            if (resultSet.next ()) {
                throw new DataSourceException ( "Genre already exists", null );
            } else {
                insertIntoGenres = connection.prepareStatement ( INSERT_GENRES );
                insertIntoGenres.setString ( 1, genreName );
                int affectedRows = insertIntoGenres.executeUpdate ();
                if (affectedRows != 1) {
                    throw new DataSourceException ( "Couldn't insert Genre!", null );
                }
            }
        } catch (SQLException e) {
            throw new DataSourceException ( "Exception in insert iIntoGenres method ", e );
        } finally {

            sourceUtils.closeResources ( insertIntoGenres );
            sourceUtils.closeResources ( resultSet );
            sourceUtils.closeResources ( queryGenre );
            sourceUtils.closeResources ( connection );
        }
    }

    public void insertIntoBooks(String isbn, String name, String genreName) throws DataSourceException {

        Connection connection = null;
        PreparedStatement insertIntoBooks = null;

        try {
            connection = DriverManager.getConnection ( CONNECTION_STRING );
            connection.setAutoCommit ( false );

            int genreId = sourceUtils.generateGenreId ( connection, genreName, QUERY_GENRE, INSERT_GENRES );

            insertIntoBooks = connection.prepareStatement ( INSERT_BOOKS );
            insertIntoBooks.setString ( 1, isbn );
            insertIntoBooks.setString ( 2, name );
            insertIntoBooks.setInt ( 3, genreId );
            int affectedRows = insertIntoBooks.executeUpdate ();
            if (affectedRows == 1) {
                connection.commit ();
            } else {
                throw new DataSourceException ( "The book insert failed, check ISBN", null );
            }
        } catch (SQLException e) {
            throw new DataSourceException ( "The book insert failed", e );
        } finally {
            sourceUtils.closeResources ( insertIntoBooks );
            sourceUtils.connectionAcTrueRollClose ( connection );
        }

    }

    public void updateBook(String bookOldName, String newIsbn, String newBookTitle, String genreName) throws DataSourceException {
        Connection connection = null;
        PreparedStatement updateBook = null;

        try {
            connection = DriverManager.getConnection ( CONNECTION_STRING );
            connection.setAutoCommit ( false );

            int genreId = sourceUtils.generateGenreId ( connection, genreName, QUERY_GENRE, INSERT_GENRES );

            updateBook = connection.prepareStatement ( UPDATE_BOOKS );
            updateBook.setString ( 1, newIsbn );
            updateBook.setString ( 2, newBookTitle );
            updateBook.setString ( 4, bookOldName );
            updateBook.setInt ( 3, genreId );

            int affectedRows = updateBook.executeUpdate ();
            if (affectedRows == 1) {
                connection.commit ();
            } else {
                throw new DataSourceException ( "update failed", null );
            }
        } catch (SQLException e) {
            throw new DataSourceException ( "couldn't update book", e );
        } finally {
            sourceUtils.closeResources ( updateBook );
            sourceUtils.connectionAcTrueRollClose ( connection );
        }
    }

    public void deleteFromBooks(String bookTitle) throws DataSourceException {

        Connection connection = null;
        PreparedStatement deleteFromBooks = null;
        try {
            connection = DriverManager.getConnection ( CONNECTION_STRING );
            deleteFromBooks = connection.prepareStatement ( DELETE_BOOKS );
            deleteFromBooks.setString ( 1, bookTitle );
            int affectedRows = deleteFromBooks.executeUpdate ();
            if (affectedRows < 1) {
                throw new DataSourceException ( "Deleted Nothing!", null );
            }
        } catch (SQLException e) {
            throw new DataSourceException ( "Exception in deleteFromBooks ", e );
        } finally {
            sourceUtils.closeResources ( deleteFromBooks );
            sourceUtils.closeResources ( connection );
        }
    }
}
