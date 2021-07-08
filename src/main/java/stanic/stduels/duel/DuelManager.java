package stanic.stduels.duel;

import stanic.stduels.Main;
import stanic.stduels.database.impl.IDatabase;
import stanic.stduels.duel.match.Match;
import stanic.stduels.duel.player.PlayerDuel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DuelManager {

    private final Map<UUID, PlayerDuel> accounts = new LinkedHashMap<>();
    private final Map<Integer, Match> matches = new LinkedHashMap<>();

    public Match createMatch() {
        Match match = new Match(matches.size() + 1);
        matches.put(match.getMatchId(), match);
        return match;
    }
    public void removeMatch(Match match) {
        matches.remove(match.getMatchId());
    }

    public Optional<PlayerDuel> getPlayerDuel(UUID uuid) {
        return Optional.ofNullable(accounts.get(uuid));
    }
    public PlayerDuel createOrGetPlayerDuel(UUID uuid) {
        if (accounts.containsKey(uuid)) return accounts.get(uuid);

        PlayerDuel model = new PlayerDuel(uuid, 0, 0, 0);
        accounts.put(uuid, model);

        return model;
    }

    public Optional<Match> getMatchByPlayer(UUID uuid) {
        return matches.values().stream()
                .filter(it -> it.getMatchPlayer(uuid).isPresent())
                .findFirst();
    }
    public Optional<Match> getMatchById(int matchId) {
        return Optional.ofNullable(matches.get(matchId));
    }

    public boolean isInDuel(UUID uuid) {
        return matches.values().stream().anyMatch(it -> it.getMatchPlayer(uuid).isPresent());
    }

    public List<PlayerDuel> getAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public void loadAccounts() throws SQLException {
        IDatabase database = Main.getInstance().getDb();
        database.open();

        Statement statement = database.getStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM duels");
        while (resultSet.next()) {
            UUID uuid = UUID.fromString(resultSet.getString("uuid"));
            int wins = resultSet.getInt("wins");
            int losses = resultSet.getInt("losses");
            int draws = resultSet.getInt("draws");

            accounts.put(uuid, new PlayerDuel(uuid, wins, losses, draws));
        }

        resultSet.close();
        statement.close();
        database.close();
    }

}
