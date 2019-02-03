package com.miloszmatejko.model;

public class Genre {

    private int genre_id;
    private String genre_name;


    private Genre(int genre_id, String genre_name) {
        this.genre_id = genre_id;
        this.genre_name = genre_name;
    }

    public int getGenre_id() {
        return genre_id;
    }

    public String getGenre_name() {
        return genre_name;
    }


    public static Genre createGenre(int genre_id, String genre_name) {
        return new Genre ( genre_id, genre_name );

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
        if (obj.getClass () == null || this.getClass () != (obj.getClass ())) {
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
