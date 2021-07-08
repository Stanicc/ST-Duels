package stanic.stduels.factory;

import stanic.stduels.Main;
import stanic.stduels.database.impl.IDatabase;
import stanic.stduels.duel.player.PlayerDuel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DuelFactory {

    public void save() throws SQLException {
        IDatabase database = Main.getInstance().getDb();
        database.open();

        Statement statement = database.getStatement();
        for (PlayerDuel account : Main.getInstance().getDuelManager().getAccounts()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM duels WHERE uuid='" + account.getUUID().toString() + "'");
            if (resultSet.next()) statement.executeUpdate(String.format("UPDATE duels SET wins='%s', losses='%s', draws='%s' WHERE uuid='%s'", account.getWins(), account.getLosses(), account.getDraws(), account.getUUID().toString()));
            else statement.execute(String.format("INSERT INTO duels (uuid, wins, losses, draws) VALUES ('%s', '%s', '%s', '%s')", account.getUUID().toString(), account.getWins(), account.getLosses(), account.getDraws()));
            resultSet.close();
        }

        statement.close();
        database.close();
    }

}
