package stanic.stduels.duel.match;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import stanic.stduels.Main;
import stanic.stduels.duel.match.controller.MatchController;
import stanic.stduels.duel.match.player.MatchPlayer;
import stanic.stduels.duel.match.state.MatchState;
import stanic.stduels.duel.match.task.MatchRunnable;
import stanic.stduels.duel.match.team.MatchTeam;

import java.util.*;
import java.util.stream.Collectors;

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
        setState(MatchState.STARTING);
        matchController.setupPlayers();

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), getMatchRunnable(), 0L, 20L);
        getMatchRunnable().setTask(task);
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

    public Optional<MatchPlayer> getMatchPlayer(UUID uuid) {
        return Optional.ofNullable(matchPlayers.get(uuid));
    }
    public Optional<MatchPlayer> getMatchPlayer(Player player) {
        return Optional.ofNullable(matchPlayers.get(player.getUniqueId()));
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

    public boolean isSameTeam(Player one, Player two) {
        return getMatchPlayer(one).get().getTeam() == getMatchPlayer(two).get().getTeam();
    }

    public Map<UUID, MatchPlayer> getMatchPlayers() {
        return matchPlayers;
    }
    public List<MatchPlayer> getPlayersInTeam(MatchTeam matchTeam) {
        return getMatchPlayers().values()
                .stream()
                .filter(it -> it.getTeam() == matchTeam)
                .collect(Collectors.toList());
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
