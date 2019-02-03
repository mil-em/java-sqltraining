package com.miloszmatejko.source;

import com.miloszmatejko.model.DataSourceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SourceUtils {

    public void closeResources(AutoCloseable autoCloseable) throws DataSourceException {
        if (autoCloseable != null) {
            try {
                autoCloseable.close ();
            } catch (Exception e) {
                throw new DataSourceException ( "couldn't close something ", e );
            }
        }
    }

    public void connectionAcTrueRollClose(Connection connection) throws DataSourceException {
        if (connection != null) {
            try {
                connection.rollback ();
                connection.setAutoCommit ( true );
                connection.close ();
            } catch (SQLException e) {
                throw new DataSourceException ( "couldn't close something ", e );
            }
        }
    }

public    int generateGenreId(Connection connection, String genreName, String sqlQueryGenre, String sqlInsertGenres) throws SQLException {

        PreparedStatement queryGenre = connection.prepareStatement (sqlQueryGenre );
        queryGenre.setString ( 1, genreName );
        ResultSet resultSet = queryGenre.executeQuery ();
        if (resultSet.next ()) {
            return resultSet.getInt ( 1 );
        } else {
            PreparedStatement insertIntoGenres = connection.prepareStatement ( sqlInsertGenres );
            insertIntoGenres.setString ( 1, genreName );
            ResultSet generatedKeys = insertIntoGenres.getGeneratedKeys ();
            if (generatedKeys.next ()) {
                return generatedKeys.getInt ( 1 );
            } else {
                throw new SQLException ( "Couldn't generate genreId" );
            }
        }

    }
}