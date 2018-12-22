package com.miloszmatejko.common;

public class BookOfGenre {
    private String genre;
    private String ISBN;
    private String title;

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
}
