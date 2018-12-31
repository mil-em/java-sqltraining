package com.miloszmatejko;

import com.miloszmatejko.common.BookOfGenre;
import com.miloszmatejko.common.Genre;
import com.miloszmatejko.source.DataSource;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {
        DataSource datasource = new DataSource ();
        if (!datasource.open ()) {
            System.out.println ("Error opening database " );
        }

        System.out.println ("----querying all genres-----");

        List<Genre> genres = datasource.queryGenres ();
        if (genres == null) {
            System.out.println ( "no genres" );
            return;
        }
//
        for (Genre genre : genres) {
            System.out.println (genre.toString ());
        }


        System.out.println ("-----------------querying all books of one genre--------");
        List<BookOfGenre> booksOfGenre = datasource.queryBooksOfGenre ( "Fantasy" );

        for (BookOfGenre bookOfGenre : booksOfGenre) {
            System.out.println (bookOfGenre.toString ());
        }


        System.out.println ( "-----------------inserting a new Genre-------------------" );

        System.out.println ( (datasource.insertIntoGenres ( "Romance" )) );

//    datasource.insertIntoBooks ( "9375847592834", "Harry Potter", "Novel"  );

        System.out.println ( "-------------------------updating a book----------------------" );
        datasource.updateBook ( "Sherlock Holmes", "2919191919191", "It", "Horror" );

//        datasource.deleteFromBooks ( "Harry Potter" );

        datasource.close ();

    }


}
