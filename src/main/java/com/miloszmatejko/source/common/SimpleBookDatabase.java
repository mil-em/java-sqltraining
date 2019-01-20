package com.miloszmatejko.source.common;

import java.util.List;

public interface SimpleBookDatabase {
    List queryGenres();
    List queryBooksOfGenre(String genreName);
    int insertIntoGenres(String genreName) throws DataSourceException;
    void insertIntoBooks(String isbn, String name, String genre) throws DataSourceException;
    void updateBook(String bookOldName, String newIsbn, String newBookTitle, String newGenre) throws DataSourceException;
    void deleteFromBooks(String bookTitle) throws DataSourceException;
}
