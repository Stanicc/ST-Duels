package stanic.stduels.duel.match.controller;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import stanic.stduels.Main;
import stanic.stduels.duel.DuelManager;
import stanic.stduels.duel.match.Match;
import stanic.stduels.duel.match.player.MatchPlayer;
import stanic.stduels.duel.match.state.MatchState;
import stanic.stduels.duel.match.team.MatchTeam;
import stanic.stduels.duel.player.PlayerDuel;
import stanic.stduels.utils.LocationUtils;
import stanic.stduels.utils.item.ItemUtils;

import java.util.ArrayList;
import java.util.List;

public class MatchController {

    Match match;

    public MatchController(Match match) {
        this.match = match;
    }

    public void setupPlayers() {
        List<MatchPlayer> matchPlayers = new ArrayList<>(match.getMatchPlayers().values());
        matchPlayers.subList(0, (matchPlayers.size()) / 2).forEach(it -> it.setTeam(MatchTeam.TEAM_ONE));
        matchPlayers.subList((matchPlayers.size()) / 2, matchPlayers.size()).forEach(it -> it.setTeam(MatchTeam.TEAM_TWO));

        teleport();
        hide();

        for (MatchPlayer matchPlayer : matchPlayers) {
            new ItemUtils().getKit().forEach((slot, item) -> matchPlayer.toPlayer().getInventory().setItem(slot, item));
            matchPlayer.toPlayer().updateInventory();
        }
    }
    public void teleport() {
        LocationUtils locationUtils = new LocationUtils();

        match.getPlayersInTeam(MatchTeam.TEAM_ONE).forEach(it -> it.toPlayer().teleport(locationUtils.getLocation("pos1")));
        match.getPlayersInTeam(MatchTeam.TEAM_TWO).forEach(it -> it.toPlayer().teleport(locationUtils.getLocation("pos2")));
    }
    public void hide() {
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (match.getMatchPlayer(target).isPresent()) continue;
            match.getMatchPlayers().values().forEach(it -> target.hidePlayer(it.toPlayer()));
        }
    }

    public void finishMatch(MatchTeam winner) {
        DuelManager duelManager = Main.getInstance().getDuelManager();
        match.setState(MatchState.FINISHING);
        match.getMatchRunnable().getTask().cancel();

        if (winner != null) {
            MatchTeam loser = winner == MatchTeam.TEAM_ONE ? MatchTeam.TEAM_TWO : MatchTeam.TEAM_ONE;

            match.getPlayersInTeam(winner).forEach(it -> {
                PlayerDuel playerDuel = duelManager.createOrGetPlayerDuel(it.getUUID());
                playerDuel.setWins(playerDuel.getWins() + 1);
            });
            match.getPlayersInTeam(loser).forEach(it -> {
                PlayerDuel playerDuel = duelManager.createOrGetPlayerDuel(it.getUUID());
                playerDuel.setLosses(playerDuel.getLosses() + 1);
            });

            sendFinalNotifications(winner, loser);
        } else {
            match.getMatchPlayers().values().forEach(it -> {
                PlayerDuel playerDuel = duelManager.createOrGetPlayerDuel(it.getUUID());
                playerDuel.setDraws(playerDuel.getDraws() + 1);
            });

            sendFinalNotifications(null, null);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                match.getMatchPlayers().values().forEach(target -> {
                    Player player = target.toPlayer();
                    player.teleport(new LocationUtils().getLocation("pos1"));

                    player.getInventory().clear();
                    player.updateInventory();
                });
            }
        }.runTaskLater(Main.getInstance(), 100L);
    }

    public void sendNotifications() {
        //send messages
    }

    public void sendFinalNotifications(MatchTeam winner, MatchTeam loser) {
        //send messages
    }

}