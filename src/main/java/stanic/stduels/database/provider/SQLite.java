package stanic.stduels.database.provider;

import stanic.stduels.database.impl.IDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLite implements IDatabase {

    private final String path;

    public SQLite(String path) {
        this.path = path;
    }

    private Connection connection;
    private Statement statement;

    public Connection getConnection() {
        return connection;
    }

    @Override
    public Statement getStatement() {
        return statement;
    }

    @Override
    public String getType() {
        return "SQLite";
    }

    @Override
    public boolean open() {
        try {
            if (this.connection == null) {
                Class.forName("org.sqlite.JDBC").newInstance();
                this.connection = DriverManager
                        .getConnection("jdbc:sqlite:" + path + "/database.db");
            }
            if (this.statement == null && this.connection != null) {
                this.statement = this.connection.createStatement();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return this.connection();
    }

    @Override
    public boolean connection() {
        return this.connection != null;
    }

    @Override
    public boolean close() {
        if (connection()) {
            try {
                if (this.statement != null)
                    this.statement.close();
                if (this.connection != null)
                    this.connection.close();

                this.statement = null;
                this.connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection();
    }

}