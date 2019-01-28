package com.miloszmatejko.view;

import com.miloszmatejko.model.BookOfGenre;
import com.miloszmatejko.model.Genre;

import java.util.List;

public class DataView {

    public void printGenres(List<Genre> genres) {
        System.out.println ("----querying all genres-----");

        if (genres == null) {
            System.out.println ( "no genres" );
            return;
        }

        for (Genre genre : genres) {
            System.out.println (genre.toString ());
        }
    }

    public void printBooksOfGenre(List<BookOfGenre> booksOfGenre) {
        System.out.println ("-----------------querying all books of one genre--------");
        for (BookOfGenre bookOfGenre : booksOfGenre) {
            System.out.println (bookOfGenre.toString ());
        }
    }
}
