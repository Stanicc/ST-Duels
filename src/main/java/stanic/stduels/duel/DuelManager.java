package stanic.stduels.duel;

import stanic.stduels.duel.match.Match;
import stanic.stduels.duel.player.PlayerDuel;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

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

    public PlayerDuel getPlayerDuel(UUID uuid) {
        return accounts.get(uuid);
    }

    public Integer loadAccounts() {
        return 0;
    }

}
