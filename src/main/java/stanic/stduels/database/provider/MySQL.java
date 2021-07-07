package stanic.stduels.database.provider;

import org.bukkit.configuration.file.FileConfiguration;
import stanic.stduels.database.impl.IDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL implements IDatabase {

    private String url, user, password;

    public MySQL(FileConfiguration config) {
        this(config.getString("Database.host"), 3306,
                config.getString("Database.database"),
                config.getString("Database.username"), config.getString("Database.password"));
    }
    public MySQL(String host, int port, String database, String user, String password) {
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true";
        this.user = user;
        this.password = password;
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
        return "MySQL";
    }

    @Override
    public boolean open() {
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            if (this.connection == null) {
                this.connection = DriverManager.getConnection(url, user, password);
            }
            if (this.statement == null && this.connection != null) {
                this.statement = this.connection.createStatement();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return connection();
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

    @Override
    public boolean connection() {
        return this.connection != null;
    }

}