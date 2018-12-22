package com.miloszmatejko;

import com.miloszmatejko.common.BookOfGenre;
import com.miloszmatejko.common.Genre;
import com.miloszmatejko.source.Datasource;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Datasource datasource = new Datasource ();

        System.out.println ("----querying all genres-----");

        List<Genre> genres = datasource.queryGenres ();
        if (genres == null) {
            System.out.println ( "no genres" );
            return;
        }

        for (Genre genre : genres) {
            System.out.println (genre.toString ());
        }


        System.out.println ("-----------------querying all books of one genre--------");
        List<BookOfGenre> booksOfGenre = datasource.queryBooksOfGenre ( "Horror" );

        for (BookOfGenre bookOfGenre : booksOfGenre) {
            System.out.println (bookOfGenre.toString ());
        }

    }

}
