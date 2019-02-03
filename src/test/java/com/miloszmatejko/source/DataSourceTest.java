package com.miloszmatejko.source;

import com.miloszmatejko.model.BookOfGenre;
import com.miloszmatejko.model.DataSourceException;
import com.miloszmatejko.model.Genre;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataSourceTest {

    private static DataSource dataSource;

    @BeforeAll
    static void setUp() throws DataSourceException {
        dataSource = new DataSource ();

    }



    @Test
    void queryGenres() throws DataSourceException {
        List<Genre> queriedGenres = dataSource.queryAllGenres ();
        List<Genre> genres = new ArrayList<> ();
        genres.add ( Genre.createGenre ( 1, "Fantasy" ) );
        genres.add ( Genre.createGenre ( 2, "SciFi" ) );
        genres.add ( Genre.createGenre ( 3, "Horror" ) );
        genres.add ( Genre.createGenre ( 5, "Scholastic" ) );
        genres.add ( Genre.createGenre ( 6, "Crime Story" ) );
        genres.add ( Genre.createGenre ( 8, "Romance" ) );
        genres.add ( Genre.createGenre ( 9, "Novel" ) );

        assertEquals ( genres, queriedGenres );

    }




        @Test @Disabled
    void deleteFromBooks() throws Exception{
            dataSource.deleteFromBooks ( "Harry Potter" );
    }
    @Test
    void insertIntoBooks() {

        Assertions.assertThrows ( DataSourceException.class, () ->
                dataSource.insertIntoBooks ( "9375847592834", "Harry Potter", "Fantasy" ) );
    }


    @Test
    void updateBook() throws Exception {
        dataSource.updateBook ("It", "111111111111", "It", "Horror");
    }

    @Test
    void queryBooksOfGenre() throws DataSourceException {
        String genre = "Fantasy";
        List <BookOfGenre> result = dataSource.queryBooksOfGenre ( genre );
        List<BookOfGenre> expected = new ArrayList<> ();
        expected.add ( BookOfGenre.createBookOfGenre ("Fantasy","1234567890125","Lord Of The Rings" ) );
        expected.add (  BookOfGenre.createBookOfGenre ("Fantasy","9375847592834","Harry Potter" ) );
        assertEquals ( expected, result );


    }

    @AfterAll
    static void tearDown() throws DataSourceException {

    }
}