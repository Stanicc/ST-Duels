package stanic.stduels.duel.match;

import org.bukkit.entity.Player;
import stanic.stduels.duel.match.controller.MatchController;
import stanic.stduels.duel.match.player.MatchPlayer;
import stanic.stduels.duel.match.state.MatchState;
import stanic.stduels.duel.match.task.MatchRunnable;

import java.util.*;

public class Match {

    private final int matchId;

    public Match(int matchId) {
        this.matchId = matchId;
    }

    private final MatchRunnable matchRunnable = new MatchRunnable(this);
    private final MatchController matchController = new MatchController(this);

    private final Map<UUID, MatchPlayer> matchPlayers = new LinkedHashMap<>();
    private final List<Player> spectators = new ArrayList<>();

    private MatchState state = MatchState.WAITING;

    public void startMatch() {
    }

    public void addSpectator(Player player) {
        spectators.add(player);
    }
    public void removeSpectator(Player player) {
        spectators.remove(player);
    }
    public List<Player> getSpectators() {
        return spectators;
    }

    public MatchPlayer getMatchPlayer(UUID uuid) {
        return matchPlayers.get(uuid);
    }
    public MatchPlayer getMatchPlayer(Player player) {
        return matchPlayers.get(player.getUniqueId());
    }

    public MatchRunnable getMatchRunnable() {
        return matchRunnable;
    }

    public MatchController getMatchController() {
        return matchController;
    }

    public MatchPlayer addPlayer(Player player) {
        MatchPlayer matchPlayer = new MatchPlayer(player.getUniqueId());
        matchPlayers.put(matchPlayer.getUUID(), matchPlayer);

        return matchPlayer;
    }
    public Map<UUID, MatchPlayer> getMatchPlayers() {
        return matchPlayers;
    }

    public MatchState getState() {
        return state;
    }

    public void setState(MatchState state) {
        this.state = state;
    }

    public int getMatchId() {
        return matchId;
    }

}
