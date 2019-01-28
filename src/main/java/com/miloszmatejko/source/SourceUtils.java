package com.miloszmatejko.source;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SourceUtils {

    public void closeResources(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close ();
            } catch (IOException e) {
                System.out.println ( "couldn't close something:" );
                e.printStackTrace ();
            }
        }
    }

    public void closeResources(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close ();
            } catch (Exception e) {
                System.out.println ( "couldn't close something:" );
                e.printStackTrace ();
            }
        }
    }

    public void autoCommitTrueAndRollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback ();
                connection.setAutoCommit ( true );

            } catch (SQLException e) {
                System.out.println ( "couldn't rollback or reset autocommit" );
                e.printStackTrace ();
            }
        }
    }
}
