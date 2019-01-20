package com.miloszmatejko.controller;

import com.miloszmatejko.source.common.BookOfGenre;
import com.miloszmatejko.source.common.DataSourceException;
import com.miloszmatejko.source.common.Genre;
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
            dataSource.open ();
            genres = dataSource.queryGenres ();
            dataSource.close ();
        } catch (DataSourceException e) {
            throw new ControllerException ( "Couldn't open database" );
        }
        return genres;
    }

    public void updateGenresView() throws ControllerException {
        try {
            dataView.printGenres ( getAllGenres () );
        } catch (ControllerException e) {
            throw new ControllerException ( e.getMessage () );
        }
    }

    public List<BookOfGenre> getAllBooksOfGenre(String genre) throws ControllerException {
        List<BookOfGenre> books ;
        try {
            dataSource.open ();
            books = dataSource.queryBooksOfGenre ( genre );
            dataSource.close ();
        } catch (DataSourceException e) {
            throw new ControllerException ( "Couldn't open database" );
        }
        return books;
    }

    public void updateBooksOfGenreView(String genre) throws ControllerException {

        dataView.printBooksOfGenre ( getAllBooksOfGenre ( genre ) );
    }

    public void insertNewGenre(String name) throws ControllerException {

        try {
            dataSource.open ();
            if (dataSource.insertIntoGenres ( "Romance" ) != 0) {
                throw new ControllerException ( "Genre already exists" );
            }
            dataSource.close ();
        } catch (DataSourceException e) {
            throw new ControllerException ( "Couldn't open database" );
        }
    }

    public void insertNewBook(String isbn, String title, String genre) throws ControllerException {

        try {
            dataSource.open ();
            dataSource.insertIntoBooks ( isbn, title, genre );
            dataSource.close ();
        } catch (DataSourceException e) {
            throw new ControllerException ( "Couldn't open database" );
        }
    }

    public void updateBook(String oldTitle, String newIsbnm, String newTitle, String newGenre) throws ControllerException {
        try {
            dataSource.open ();
            dataSource.updateBook ( "Sherlock Holmes", "2919191919191", "It", "Horror" );
            dataSource.close ();
        } catch (DataSourceException e) {
            throw new ControllerException ( "couldn't update book " + e.getMessage () );
        }
    }

    public void deleteBook (String title) throws ControllerException {
        try {
            dataSource.open ();
            dataSource.deleteFromBooks ( title );
            dataSource.close ();
        } catch (DataSourceException e) {
            throw new ControllerException ( e.getMessage () );
        }
    }

}



