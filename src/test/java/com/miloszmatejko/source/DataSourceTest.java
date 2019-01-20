package com.miloszmatejko.source;

import com.miloszmatejko.source.common.BookOfGenre;
import com.miloszmatejko.source.common.DataSourceException;
import com.miloszmatejko.source.common.Genre;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataSourceTest {

    private static DataSource dataSource;

    @BeforeAll
    static void setUp() throws DataSourceException {
        dataSource = new DataSource ();
        dataSource.open ();
    }



    @Test
    void queryGenres() throws DataSourceException {
        List<Genre> queriedGenres = dataSource.queryGenres ();
        List<Genre> genres = new ArrayList<> ();
        genres.add ( new Genre ( 1, "Fantasy" ) );
        genres.add ( new Genre ( 2, "SciFi" ) );
        genres.add ( new Genre ( 3, "Horror" ) );
        genres.add ( new Genre ( 5, "Scholastic" ) );
        genres.add ( new Genre ( 6, "Crime Story" ) );
        genres.add ( new Genre ( 8, "Romance" ) );
        genres.add ( new Genre ( 9, "Novel" ) );

        assertEquals ( genres, queriedGenres );

    }


    @Test
    void insertIntoGenres() throws Exception {

        int num = dataSource.insertIntoGenres ( "Romance" );
        assertEquals ( 8, num );

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
        expected.add ( new BookOfGenre ("Fantasy","1234567890125","Lord Of The Rings" ) );
        expected.add ( new BookOfGenre ("Fantasy","9375847592834","Harry Potter" ) );
        assertEquals ( expected, result );


    }

    @AfterAll
    static void tearDown() throws DataSourceException {
        dataSource.close ();
    }
}