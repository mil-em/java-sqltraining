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
//            e.printStackTrace ();

        }

        try {
            controller.updateBooksOfGenreView ( "Horror" );
        } catch (ControllerException e) {
            System.out.println (e.getMessage ());
//            e.printStackTrace ();
        }

        try {
            controller.insertNewGenre ( "Romance" );
        } catch (ControllerException e) {
            System.out.println ( e.getMessage () );
//        e.printStackTrace ();

        }

        try {
            controller.insertNewBook ( "888888888888", "Cujo", "Horror" );
        } catch (ControllerException e) {
            System.out.println (e.getMessage ());
//            e.printStackTrace ();
        }

        try {
            controller.updateBook ( "Cujo", "0000000000000", "Sherlock Holmes", "Crime Story" );
        } catch (ControllerException e) {
            System.out.println (e.getMessage ());
//            e.printStackTrace ();

        }

        try {
         controller.deleteBook ( "Sherlock Holmes" );
        } catch (ControllerException e) {
            System.out.println ( e.getMessage () );
//        e.printStackTrace ();

        }

    }


}
