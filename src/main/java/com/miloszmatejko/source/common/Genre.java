package com.miloszmatejko.source.common;

public class Genre {

    private int genre_id;
    private String genre_name;

    public Genre() {}

    public Genre(int genre_id, String genre_name) {
        this.genre_id = genre_id;
        this.genre_name = genre_name;
    }

    public int getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(int genre_id) {
        this.genre_id = genre_id;
    }

    public String getGenre_name() {
        return genre_name;
    }

    public void setGenre_name(String genre_name) {
        this.genre_name = genre_name;
    }

    @Override
    public String toString() {
        return
                "genre_id = " + this.genre_id +
                ", genre_name = '" + this.genre_name + '\''
               ;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj.getClass ()==null || this.getClass ()!= ( obj.getClass () )) {
            return false;
        }
        Genre g = (Genre) obj;
        return (this.genre_name.equals ( g.genre_name )) && (this.genre_id == g.genre_id);
//        return this.toString ().equals ( obj.toString () );
    }

    @Override
    public int hashCode() {
        int number = 17;
        number = number * 31 + this.genre_id;
        return number * 31 + this.genre_name.hashCode ();

    }
}
