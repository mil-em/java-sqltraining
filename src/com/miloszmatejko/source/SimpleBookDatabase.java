package com.miloszmatejko.source;

import java.sql.SQLException;
import java.util.List;

public interface SimpleBookDatabase {
    List queryGenres();
    List queryBooksOfGenre(String genreName);
    int insertIntoGenres(String genreName) throws SQLException;
    void insertIntoBooks(String isbn, String name, String genre) throws SQLException;
    void updateBook(String bookOldName, String newIsbn, String newBookTitle, String newGenre) throws SQLException;
    void deleteFromBooks(String bookTitle) throws SQLException;
}
