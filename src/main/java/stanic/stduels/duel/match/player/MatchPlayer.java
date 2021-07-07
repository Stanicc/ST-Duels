package stanic.stduels.duel.match.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import stanic.stduels.duel.match.team.MatchTeam;

import java.util.UUID;

public class MatchPlayer {

    private final UUID uuid;
    private MatchTeam team;

    public MatchPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }
    public Player toPlayer() {
        return Bukkit.getPlayer(getUUID());
    }

    public MatchTeam getTeam() {
        return team;
    }

    public void setTeam(MatchTeam team) {
        this.team = team;
    }

}
