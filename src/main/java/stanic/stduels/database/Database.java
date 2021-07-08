package stanic.stduels.database;

import stanic.stduels.Main;
import stanic.stduels.database.impl.IDatabase;
import stanic.stduels.database.provider.MySQL;
import stanic.stduels.database.provider.SQLite;
import stanic.stduels.task.SaveTask;

import java.sql.SQLException;

public class Database {

    public void start() throws SQLException {
        if (Main.getInstance().getSettings().getBoolean("Database.mysql")) {
            Main.getInstance().setDatabase(new MySQL(Main.getInstance().getSettings()));
        } else Main.getInstance().setDatabase(new SQLite(Main.getInstance().getDataFolder().getPath()));

        IDatabase database = Main.getInstance().getDb();
        if (database.open()) {
            database.getStatement().execute("CREATE TABLE IF NOT EXISTS duels (uuid TEXT, wins INT, losses INT, draws INT)");
            database.close();
        }

        Main.getInstance().getDuelManager().loadAccounts();
        new SaveTask().run();
    }

}
