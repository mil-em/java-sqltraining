package com.miloszmatejko.source;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Datasource {


    public static final String DB_NAME = "database.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\milosz\\projects\\java-sqltraining\\database.db";

    private Connection connection;

    private PreparedStatement insertNewGenre;
    private PreparedStatement insertNewBook;
}
