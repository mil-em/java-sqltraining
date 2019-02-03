package com.miloszmatejko.model;

import java.util.List;

public interface SimpleBookDatabase {
    List queryAllGenres() throws DataSourceException;
    List queryBooksOfGenre(String genreName) throws DataSourceException;
    void insertIntoGenres(String genreName) throws DataSourceException;
    void insertIntoBooks(String isbn, String name, String genre) throws DataSourceException;
    void updateBook(String bookOldName, String newIsbn, String newBookTitle, String newGenre) throws DataSourceException;
    void deleteFromBooks(String bookTitle) throws DataSourceException;
}
