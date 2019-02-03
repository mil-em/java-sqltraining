package com.miloszmatejko.controller;

import com.miloszmatejko.model.BookOfGenre;
import com.miloszmatejko.model.DataSourceException;
import com.miloszmatejko.model.Genre;
import com.miloszmatejko.source.DataSource;
import com.miloszmatejko.view.DataView;

import java.util.List;

public class DataController {
    private DataSource dataSource;
    private DataView dataView;

    public DataController(DataSource dataSource, DataView dataView) {
        this.dataSource = dataSource;
        this.dataView = dataView;
    }

    public List<Genre> getAllGenres() throws ControllerException {
        List<Genre> genres;
        try {
            genres = dataSource.queryAllGenres ();
        } catch (DataSourceException e) {
            throw new ControllerException ( e.getMessage () ,e );
        }
        return genres;
    }

    public void updateGenresView() throws ControllerException {
        try {
            dataView.printGenres ( getAllGenres () );
        } catch (ControllerException e) {
            throw new ControllerException ( e.getMessage (), e );
        }
    }

    public List<BookOfGenre> getAllBooksOfGenre(String genre) throws ControllerException {
        List<BookOfGenre> books ;
        try {

            books = dataSource.queryBooksOfGenre ( genre );

        } catch (DataSourceException e) {
            throw new ControllerException ( e.getMessage (),e );
        }
        return books;
    }

    public void updateBooksOfGenreView(String genre) throws ControllerException {

        dataView.printBooksOfGenre ( getAllBooksOfGenre ( genre ) );
    }

    public void insertNewGenre(String name) throws ControllerException {

        try {

            dataSource.insertIntoGenres ( name ) ;

        } catch (DataSourceException e) {
            throw new ControllerException (e.getMessage (),e );
        }
    }

    public void insertNewBook(String isbn, String title, String genre) throws ControllerException {

        try {

            dataSource.insertIntoBooks ( isbn, title, genre );

        } catch (DataSourceException e) {
            throw new ControllerException ( e.getMessage (), e  );
        }
    }

    public void updateBook(String oldTitle, String newIsbn, String newTitle, String newGenre) throws ControllerException {
        try {

            dataSource.updateBook ( oldTitle, newIsbn, newTitle, newGenre );

        } catch (DataSourceException e) {
            throw new ControllerException (e.getMessage (),e );
        }
    }

    public void deleteBook (String title) throws ControllerException {
        try {

            dataSource.deleteFromBooks ( title );

        } catch (DataSourceException e) {
            throw new ControllerException ( e.getMessage (),e  );
        }
    }

}



