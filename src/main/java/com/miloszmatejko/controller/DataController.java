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
        if (!dataSource.open ()) {
            throw new ControllerException ( "Couldn't open database" );
        }
        genres = dataSource.queryGenres ();
        dataSource.close ();
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
        if (!dataSource.open ()) {
            throw new ControllerException ( "Couldn't open database" );
        }
        books = dataSource.queryBooksOfGenre ( genre );
        dataSource.close ();
        return books;
    }

    public void updateBooksOfGenreView(String genre) throws ControllerException {

        dataView.printBooksOfGenre ( getAllBooksOfGenre ( genre ) );
    }

    public void insertNewGenre(String name) throws ControllerException {

        if (!dataSource.open ()) {
            throw new ControllerException ( "Couldn't open database" );
        }
        try {
            if (dataSource.insertIntoGenres ( "Romance" ) != 0) {
                throw new ControllerException ( "Genre already exists" );
            }
        } catch (DataSourceException e) {
            System.out.println ( e.getMessage () );
            throw new ControllerException ( "couldn't insert a new Genre" );
        }
    }

    public void insertNewBook(String isbn, String title, String genre) throws ControllerException {
        try {
            dataSource.insertIntoBooks ( isbn, title, genre );

        } catch (DataSourceException e) {
            throw new ControllerException ( "couldn't insert new Book " + e.getMessage () );
        }
    }

    public void updateBook(String oldTitle, String newIsbnm, String newTitle, String newGenre) throws ControllerException {
        try {
            dataSource.updateBook ( "Sherlock Holmes", "2919191919191", "It", "Horror" );
        } catch (DataSourceException e) {
            throw new ControllerException ( "couldn't update book " + e.getMessage () );
        }
    }

    public void deleteBook (String title) throws ControllerException {
        try {
            dataSource.deleteFromBooks ( title );
        } catch (DataSourceException e) {
            throw new ControllerException ( e.getMessage () );
        }
    }

}



