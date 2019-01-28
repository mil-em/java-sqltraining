package com.miloszmatejko.model;

public class BookOfGenre {
    private String genre;
    private String ISBN;
    private String title;

    public BookOfGenre(){}

    public BookOfGenre(String genre, String ISBN, String title) {
        this.genre = genre;
        this.ISBN = ISBN;
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
