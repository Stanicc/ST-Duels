package stanic.stduels.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import stanic.stduels.Main;
import stanic.stduels.duel.match.Match;
import stanic.stduels.duel.match.player.MatchPlayer;
import stanic.stduels.duel.match.team.MatchTeam;

import java.util.List;

public class DuelListeners implements Listener {

    private final Main main;

    public DuelListeners(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (main.getDuelManager().isInDuel(event.getEntity().getUniqueId()) && event.getEntity().getKiller() != null) {
            Player player = event.getEntity();

            Match match = main.getDuelManager().getMatchByPlayer(player.getUniqueId()).get();
            MatchPlayer matchPlayer = match.getMatchPlayer(player.getUniqueId()).get();
            matchPlayer.setAlive(false);

            MatchTeam anotherTeam = matchPlayer.getTeam() == MatchTeam.TEAM_ONE ? MatchTeam.TEAM_TWO : MatchTeam.TEAM_ONE;
            List<MatchPlayer> playerTeamAlive = match.getPlayersAlive(matchPlayer.getTeam());
            List<MatchPlayer>  anotherTeamAlive = match.getPlayersAlive(anotherTeam);
            if (playerTeamAlive.isEmpty() && anotherTeamAlive.isEmpty()) {
                match.getMatchController().finishMatch(null);
                return;
            }

            if (anotherTeamAlive.isEmpty()) match.getMatchController().finishMatch(matchPlayer.getTeam());
            else if (playerTeamAlive.isEmpty()) match.getMatchController().finishMatch(anotherTeam);
            else match.addSpectator(player);
        }
    }

}
