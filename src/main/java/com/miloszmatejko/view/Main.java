package com.miloszmatejko.view;

import com.miloszmatejko.controller.DataController;
import com.miloszmatejko.controller.ControllerException;
import com.miloszmatejko.source.DataSource;

public class Main {

    public static void main(String[] args){
        DataSource datasource = new DataSource ();
        DataView view = new DataView ();
        DataController controller = new DataController ( datasource, view );

        try {
            controller.updateGenresView ();
        } catch (ControllerException e) {
            System.out.println (e.getMessage ());
        }

        try {
            controller.updateBooksOfGenreView ( "Fantasy" );
        } catch (ControllerException e) {
            System.out.println (e.getMessage ());
        }

        try {
            controller.insertNewGenre ( "Romance" );
        } catch (ControllerException e) {
            System.out.println ( e.getMessage () );
        }

        try {
            controller.insertNewBook ( "9375847592834", "Harry Potter", "Fantasy" );
        } catch (ControllerException e) {
            System.out.println (e.getMessage ());
        }

        try {
            controller.updateBook ( "Sherlock Holmes", "2919191919191", "It", "Horror" );
        } catch (ControllerException e) {
            System.out.println (e.getMessage ());
        }

        try {
            controller.deleteBook ( "Sherlock Holmes" );
        } catch (ControllerException e) {
            System.out.println ( e.getMessage () );
        }

    }


}
