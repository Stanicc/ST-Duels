package stanic.stduels.duel.match.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import stanic.stduels.duel.match.Match;
import stanic.stduels.duel.match.team.MatchTeam;

import java.util.UUID;

public class MatchPlayer {

    private final UUID uuid;
    private final Match match;
    private MatchTeam team;
    private boolean isAlive;

    public MatchPlayer(UUID uuid, Match match) {
        this.uuid = uuid;
        this.match = match;
    }

    public UUID getUUID() {
        return uuid;
    }
    public Player toPlayer() {
        return Bukkit.getPlayer(getUUID());
    }

    public Match getMatch() {
        return match;
    }

    public MatchTeam getTeam() {
        return team;
    }

    public void setTeam(MatchTeam team) {
        this.team = team;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

}
