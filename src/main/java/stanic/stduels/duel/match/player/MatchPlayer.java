package stanic.stduels.duel.match.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MatchPlayer {

    private final UUID uuid;

    public MatchPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }
    public Player toPlayer() {
        return Bukkit.getPlayer(getUUID());
    }

}
