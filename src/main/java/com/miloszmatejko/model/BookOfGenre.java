package com.miloszmatejko.model;

import java.time.chrono.IsoChronology;

public class BookOfGenre {
    private String genre;
    private String ISBN;
    private String title;



    private BookOfGenre(String genre, String ISBN, String title) {
        this.genre = genre;
        this.ISBN = ISBN;
        this.title = title;
    }

    public static BookOfGenre createBookOfGenre(String genre, String ISBN, String title) {
        return new BookOfGenre ( genre, ISBN, title );
    }

    @Override
    public String toString() {
        return
                "genre = '" + genre + '\'' +
                ", ISBN = '" + ISBN + '\'' +
                ", title = '" + title + '\'';

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj.getClass ()==null || this.getClass ()!= ( obj.getClass () )) {
            return false;
        }
        BookOfGenre g = (BookOfGenre) obj;
        return (this.genre.equals ( g.genre )) && (this.ISBN.equals ( g.ISBN ));

    }

    @Override
    public int hashCode() {
        int number = 17;
        number = number * 31 + this.genre.hashCode ();
        number = number * 31 + this.ISBN.hashCode ();
        return number * 31 + this.title.hashCode ();
    }

}
