package com.miloszmatejko;

import com.miloszmatejko.source.DataSource;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        DataSource datasource = new DataSource ();

//        System.out.println ("----querying all genres-----");
//
//        List<Genre> genres = datasource.queryGenres ();
//        if (genres == null) {
//            System.out.println ( "no genres" );
//            return;
//        }
//
//        for (Genre genre : genres) {
//            System.out.println (genre.toString ());
//        }


//        System.out.println ("-----------------querying all books of one genre--------");
//        List<BookOfGenre> booksOfGenre = datasource.queryBooksOfGenre ( "Horror" );
//
//        for (BookOfGenre bookOfGenre : booksOfGenre) {
//            System.out.println (bookOfGenre.toString ());
//        }


//        System.out.println ( "-----------------inserting a new Genre-------------------" );

//        System.out.println ( (datasource.insertIntoGenres ( "Romance" )) );

//    datasource.insertIntoBooks ( "9375847592834", "Harry Potter", "Fantasy"  );

//        System.out.println ( "-------------------------updating a book----------------------" );
//        datasource.updateBook ( "The Shining", "2919191919191", "Sherlock Holmes", "Crime Story" );

        datasource.deleteFromBooks ( "Harry Potter" );
    }


}
