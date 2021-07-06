package stanic.stduels.duel.player;

import java.util.UUID;

public class PlayerDuel {

    private final UUID uuid;
    private int wins;
    private int losses;
    private int draws;

    public PlayerDuel(UUID uuid, int wins, int losses, int draws) {
        this.uuid = uuid;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

}
